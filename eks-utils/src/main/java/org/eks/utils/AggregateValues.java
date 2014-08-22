/*
 * (C) Copyright 2014 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     thibaud
 */

package org.eks.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.runtime.datasource.ConnectionHelper;

/**
 * WARNING: This is done with no ACL check, so basically, it returns all the results
 *
 * @author Thibaud Arguillere
 */
@Operation(id=AggregateValues.ID, category=Constants.CAT_SERVICES, label="AggregateValues", description="Put the JSON result in varName")
public class AggregateValues {

    public static final String ID = "AggregateValues";

    public static final Log log = LogFactory.getLog(AggregateValues.class);

    protected static final String kSQL_FOR_EVENTS = "SELECT event AS label, COUNT(event) AS count"
                                                        + " FROM appcommon ac"
                                                        + " LEFT JOIN proxies p ON p.targetid = ac.id"
                                                        + " JOIN hierarchy h ON h.id = ac.id"
                                                        + " WHERE p.targetid IS NULL "
                                                          + " AND h.isversion IS NULL"
                                                          + " AND event IS NOT NULL AND event <> ''"
                                                        + " GROUP BY event ORDER BY event";


    protected static final String kSQL_FOR_CATEGORIES = "SELECT category AS label, COUNT(category) AS count"
                                                        + " FROM appcommon ac"
                                                        + " LEFT JOIN proxies p ON p.targetid = ac.id"
                                                        + " JOIN hierarchy h ON h.id = ac.id"
                                                        + " WHERE p.targetid IS NULL "
                                                          + " AND h.isversion IS NULL"
                                                          + " AND category IS NOT NULL AND category <> ''"
                                                        + " GROUP BY category ORDER BY category";

    protected static final String kSQL_FOR_KEYWORDS = "SELECT item AS label, count(item) AS count"
                                                        + " FROM ac_keywords as ack"
                                                        + " LEFT JOIN proxies p ON p.targetid = ack.id"
                                                        + " JOIN hierarchy h ON h.id = ack.id"
                                                        + " WHERE p.targetid IS NULL "
                                                        + " AND h.isversion IS NULL"
                                                          + " AND item IS NOT NULL AND item <> ''"
                                                        + " GROUP BY item ORDER BY item";

    protected static final String kSQL_FOR_WIDTH_RANGE = "SELECT width_range AS label, COUNT(width_range) AS count"
                                                        + " FROM appcommon ac"
                                                        + " LEFT JOIN proxies p ON p.targetid = ac.id"
                                                        + " JOIN hierarchy h ON h.id = ac.id"
                                                        + " WHERE p.targetid IS NULL "
                                                          + " AND h.isversion IS NULL"
                                                          + " AND width_range IS NOT NULL"
                                                        + " GROUP BY width_range ORDER BY width_range";

    protected static final String kSQL_FOR_HEIGHT_RANGE = "SELECT height_range AS label, COUNT(height_range) AS count"
                                                        + " FROM appcommon ac"
                                                        + " LEFT JOIN proxies p ON p.targetid = ac.id"
                                                        + " JOIN hierarchy h ON h.id = ac.id"
                                                        + " WHERE p.targetid IS NULL "
                                                          + " AND h.isversion IS NULL"
                                                          + " AND width_range IS NOT NULL"
                                                        + " GROUP BY height_range ORDER BY height_range";

    @Context
    protected CoreSession session;

    @Context
    protected OperationContext ctx;

    @Param(name = "statsOnWhat", required = true, widget = Constants.W_OPTION, values = {"Events", "Categories", "EKS-Keywords", "Width Ranges", "Height Ranges", "All"})
    String statsOnWhat;

    @Param(name = "varName", required = false)
    protected String varName;

    @OperationMethod
    public void run() throws Exception {
        String jsonResult = "";
        boolean doAll = statsOnWhat.toLowerCase().equals("all");

        jsonResult = "{";
        if(doAll || statsOnWhat.toLowerCase().equals("events")) {
            jsonResult += "\"events\":" + doTheSQLQuery(kSQL_FOR_EVENTS) + ",";
        }
        if(doAll || statsOnWhat.toLowerCase().equals("categories")) {
            jsonResult += "\"categories\":" + doTheSQLQuery(kSQL_FOR_CATEGORIES) + ",";
        }
        if(doAll || statsOnWhat.toLowerCase().equals("eks-keywords")) {
            jsonResult += "\"keywords\":" + doTheSQLQuery(kSQL_FOR_KEYWORDS) + ",";
        }
        if(doAll || statsOnWhat.toLowerCase().equals("width ranges")) {
            jsonResult += "\"widthRange\":" + doTheSQLQuery(kSQL_FOR_WIDTH_RANGE) + ",";
        }
        if(doAll || statsOnWhat.toLowerCase().equals("height ranges")) {
            jsonResult += "\"heightRange\":" + doTheSQLQuery(kSQL_FOR_HEIGHT_RANGE) + ",";
        }
        jsonResult = jsonResult.substring(0, jsonResult.length() - 1);
        jsonResult += "}";

        //log.warn(jsonResult);
        ctx.put(varName, jsonResult);
    }

    protected String doTheSQLQuery(String inQueryStr) throws SQLException {
        String jsonResult = "[";

        Connection co = ConnectionHelper.getConnection(null);
        if(co != null) {
            try {
                Statement st = co.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

                ResultSet rs = st.executeQuery(inQueryStr);
                while(rs.next()) {
                    jsonResult += "{\"label\":\"" + rs.getString("label") + "\", \"count\":" + rs.getInt("count") + "},";
                }
                rs.close();
                // Remove last comma.
                // If there was nothing, then reduce to empty
                if(jsonResult.equals("[")) {
                    jsonResult = "[]";
                } else {
                    jsonResult = jsonResult.substring(0, jsonResult.length() - 1) + "]";
                }

            } catch (SQLException e) {
                // . . .
                throw e;
            } finally {
                co.close();
            }
        }

        return jsonResult;
    }

}
