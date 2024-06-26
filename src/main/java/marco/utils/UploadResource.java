package marco.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;


import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.MultipartForm;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.jboss.resteasy.reactive.server.spi.RuntimeConfiguration.Body;

import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.core.eventbus.EventBus;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("upload")
public class UploadResource {

    private static final Logger LOG = Logger.getLogger(UploadResource.class.getName());

    @Inject
    EventBus bus;

    // BAW compatible using single document payload
    @POST
    @Consumes(jakarta.ws.rs.core.MediaType.MULTIPART_FORM_DATA)    
    @APIResponse(responseCode = "202", description = "OK")
    //public Response upload(@MultipartForm MultipartBody body) throws IOException {
    public Response upload(@MultipartForm MultipartBody body) throws IOException {

        if (body != null) {

            /*
             * Array
            LOG.info("upload() quantity of files: " + body.files.size());
            for (FileUpload file : body.files) {

                LOG.info("===>> filePath: " + file.filePath());
                LOG.info("===>> fileName: " + file.fileName());
                LOG.info("===>> uploadedFile: " + file.uploadedFile());
                LOG.info("===>> name: " + file.name());
                LOG.info("===>> contentType: " + file.contentType());
                LOG.info("===>> size: " + file.size());
                LOG.info("===>> charSet: " + file.charSet());
                LOG.info("----------------------------------");

                if (file.contentType().equals("text/plain")) {
                    BufferedReader br = Files.newBufferedReader(file.filePath());
                    bus.send("file-service", br);
                }
            }
            */
            if (body.file != null) {
                LOG.info("upload() single file");
                LOG.info("===>> filePath: " + body.file.filePath());
                LOG.info("===>> fileName: " + body.file.fileName());
                LOG.info("===>> uploadedFile: " + body.file.uploadedFile());
                LOG.info("===>> name: " + body.file.name());
                LOG.info("===>> contentType: " + body.file.contentType());
                LOG.info("===>> size: " + body.file.size());
                LOG.info("===>> charSet: " + body.file.charSet());
                LOG.info("----------------------------------");

                if ( body.file.contentType() != null && body.file.contentType().equals("text/plain") ) {
                    BufferedReader br = Files.newBufferedReader(body.file.filePath());
                    bus.send("file-service", br);
                }

            LOG.info("upload() before response Accepted");
            } else {
                LOG.info("upload() body.file null");
            }
        } else {
            LOG.info("upload() body empty");
        }
        return Response
                .accepted()
                .build();
    }


    // Class that will define the OpenAPI schema for the binary type input (upload)
    @Schema(type = SchemaType.STRING, format = "binary")
    public interface UploadItemSchema {
    }

    // We instruct OpenAPI to use the schema provided by the 'UploadFormSchema'
    // class implementation and thus define a valid OpenAPI schema for the Swagger
    // UI
    /*
     * array
    public static class MultipartBody {
        @Schema(implementation = UploadItemSchema[].class)
        @RestForm("files")
        public List<FileUpload> files;
    }
     */

     // single file
    public static class MultipartBody {
        @Schema(implementation = UploadItemSchema.class)
        @RestForm("file")
        public FileUpload file;
    }

}
