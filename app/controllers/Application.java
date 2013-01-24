package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.ryantenney.passkit4j.Pass;
import com.ryantenney.passkit4j.PassResource;
import com.ryantenney.passkit4j.PassSerializer;
import com.ryantenney.passkit4j.model.*;
import com.ryantenney.passkit4j.sign.PassSigner;
import com.ryantenney.passkit4j.sign.PassSignerImpl;
import java.io.File;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Result passbook() {

        try {
            passTest();
        } catch (Exception e) {
            System.out.println(e);
        }
        response().setHeader("Pragma", "no-cache");
        response().setContentType("application/vnd.apple.pkpass");
        response().setHeader("Content-disposition", "attachment; filename=StoreCard.pkpass");
        File file = new File("/tmp/StoreCard.pkpass");
        return ok(file).as("application/vnd.apple.pkpass");
    }

    public static void passTest() throws Exception {

        Location poornerd = new Location(48.072582436845785, 11.520911265323651);
        poornerd.relevantText("You are at SiteForce");

        String teamIdentifier = Play.application().configuration().getString("passkey.teamIdentifier");
        String passTypeIdentifier = Play.application().configuration().getString("passkey.passTypeIdentifier");
        String serialNumber = Play.application().configuration().getString("passkey.serialNumber");

        String certificateFile = Play.application().configuration().getString("passkey.certificateFile");
        String passkey = Play.application().configuration().getString("passkey.passkey");
        String publicKey = Play.application().configuration().getString("passkey.publicKey");
        
        Pass pass = new Pass().teamIdentifier(teamIdentifier).passTypeIdentifier(passTypeIdentifier).organizationName("poornerd.com").description("Poornerd Coffee Rewards Card Example").serialNumber(serialNumber) 
                .locations(poornerd)
                .foregroundColor(Color.BLACK).backgroundColor(new Color(192, 192, 192)).files(
                new PassResource(Play.class.getResource("/resources/icon.png").getFile()),
                new PassResource(Play.class.getResource("/resources/icon@2x.png").getFile()),
                new PassResource(Play.class.getResource("/resources/logo.png").getFile()),
                new PassResource(Play.class.getResource("/resources/logo@2x.png").getFile()),
                new PassResource(Play.class.getResource("/resources/background.png").getFile()),
                new PassResource(Play.class.getResource("/resources/background@2x.png").getFile())).passInformation(
                new StoreCard().primaryFields(
                new TextField("welcome", "Welcome", "Welcome to the poornerd.com Pass!")).auxiliaryFields(
                new TextField("reminder", "Checkin Reminder!", "Please remember to checkin on Foursquare - links are on the back - click the 'i'")
                ).backFields(
                new TextField("terms", "Checking Links",
                "Click here to check in at SiteForce: foursquare://venues/4b534f78f964a5209f9627e3 ")));

        PassSigner signer = PassSignerImpl.builder().keystore(new FileInputStream(Play.class.getResource("publicKey").getFile()), passkey).intermediateCertificate(new FileInputStream(Play.class.getResource(certificateFile).getFile())).build();

        PassSerializer.writePkPassArchive(pass, signer, new FileOutputStream("/tmp/StoreCard.pkpass"));
    }
}