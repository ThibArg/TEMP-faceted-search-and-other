package org.nuxeo.eks;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.webengine.model.Resource;
import org.nuxeo.ecm.webengine.model.WebObject;


@Produces("text/html;charset=UTF-8")
@WebObject(type="OneDoc")
public class OneDoc extends EKSPublication{

    private static final Log log = LogFactory.getLog(OneDoc.class);

    @Override
    @GET
    public Object doGet() {
        return getView("index");
    }

    @Override
    @Path("display/{id}")
    public Resource getTheDoc(@PathParam("id") String id){
        return newObject("OneDoc", id);

    }

    @Override
    protected void initialize(Object... arg) {
        String id = (String) arg[0];
        CoreSession session = null;
        DocumentModel theDoc;
        session = ctx.getCoreSession();

        ctx.setProperty("found", false);
        ctx.setProperty("id", "");
        ctx.setProperty("title", "");
        ctx.setProperty("type", "");
        ctx.setProperty("event", "");
        ctx.setProperty("category", "");
        ctx.setProperty("width", "");
        ctx.setProperty("height", "");
        ctx.setProperty("thumbnailUrl", "");
        ctx.setProperty("pictureUrl", "");
        ctx.setProperty("downloadUrl", "");

        if (session != null) {
            try {
                DocumentModelList docs = session.query("SELECT * FROM Document WHERE ecm:uuid='" + id + "'");
                if(docs.size() > 0) {
                    theDoc = docs.get(0);

                    ctx.setProperty("found", true);
                    ctx.setProperty("id", theDoc.getId());
                    ctx.setProperty("title", theDoc.getTitle());
                    if(theDoc.getType().equals("Picture") || theDoc.getType().equals("Video")) {
                        ctx.setProperty("type", theDoc.getType());
                    }
                    if(theDoc.hasFacet("Thumbnail")) {
                        String thumbNailUrl = ctx.getServerURL() + "/nuxeo/nxthumb/default/" + id + "/blobholder:0/";
                        ctx.setProperty("thumbnailUrl", thumbNailUrl);
                    }
                    if(theDoc.hasSchema("AppCommon")) {
                        ctx.setProperty("event", theDoc.getPropertyValue("ac:event"));
                        ctx.setProperty("category", theDoc.getPropertyValue("ac:category"));
                        ctx.setProperty("width", theDoc.getPropertyValue("ac:width"));
                        ctx.setProperty("height", theDoc.getPropertyValue("ac:height"));
                    }
                    if(theDoc.getType().equals("Picture")) {
                        String pictUrl = ctx.getServerURL() + "/nuxeo/nxpicsfile/default/" + id + "/Medium:content/";
                        ctx.setProperty("pictureUrl", pictUrl);
                    }
                    if(theDoc.hasSchema("file") && theDoc.getPropertyValue("file:content") != null) {
                    // http://localhost:8080/nuxeo/nxbigfile/default/c4eb272a-7523-41fe-a110-5c7c3d16f34f/blobholder:0/null
                        String downloadUrl = ctx.getServerURL() + "/nuxeo/nxbigfile/default/" + id + "/blobholder:0/";
                        ctx.setProperty("downloadUrl", downloadUrl);
                    }
                }

            } catch (ClientException e) {
                log.error(e);
            }
        }
    }

}
