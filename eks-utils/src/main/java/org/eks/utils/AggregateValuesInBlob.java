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

import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationChain;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.impl.blob.StringBlob;

/**
 * @author Thibaud Arguillere
 */
@Operation(id=AggregateValuesInBlob.ID, category=Constants.CAT_SERVICES, label="AggregateValuesInBlob", description="")
public class AggregateValuesInBlob {

    public static final String ID = "AggregateValuesInBlob";

    @Context
    protected CoreSession session;

    @Context
    protected OperationContext ctx;

    @Context
    protected AutomationService service;

    @Param(name = "statsOnWhat", required = true, widget = Constants.W_OPTION, values = {"Events", "Categories", "EKS-Keywords", "Width Ranges", "All"})
    String statsOnWhat;

    @OperationMethod
    public Blob run() throws Exception {

        ctx.put("jsonResult", "");

        OperationChain chain = new OperationChain("AggregateValuesInBlob_" + Math.random());
        chain.add(AggregateValues.ID)
             .set("statsOnWhat", statsOnWhat)
             .set("varName", "jsonResult");
        service.run(ctx,  chain);

        String jsonResult = (String) ctx.get("jsonResult");

        return new StringBlob(jsonResult, "text/plain", "UTF-8");
    }
}
