package es.jordan.sistemaalarma;



import android.content.Intent;
import android.net.Uri;

import junit.framework.TestCase;

import org.junit.Test;

public class MenuInvitadoTest extends TestCase {
 @Test

    public void testEnlaceMail() throws Exception {

     Intent email = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
             "mailto", "emaildeprueba@gmail.com", null));
     email.putExtra(Intent.EXTRA_SUBJECT, "titulo de prueba");
     String[] addresses = {"emaildeprueba@gmail.com"};
     email.putExtra(Intent.EXTRA_EMAIL, addresses);
     email.putExtra(Intent.EXTRA_TEXT, "Esto es un test");

    }
}