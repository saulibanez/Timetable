package sibanez.mov.urjc.es.timetable;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SubjectBBDDTest {

    @Test
    public void bbddTest() throws Exception {
        // Context of the app under test.
        Context ctx = InstrumentationRegistry.getTargetContext();
        SubjectBBDD bbdd = new SubjectBBDD(ctx.getApplicationContext());

        bbdd.insertSubject(0, "saul", "ibanez", "android", "saul@saul.com", "L-V", "9:00-11:00");
        bbdd.insertSubject(1, "paco", "ibanez", "seguridad", "paco@saul.com", "M-J", "8:00-10:00");
        bbdd.insertSubject(2, "pepe", "ibanez", "robotica", "pepe@saul.com", "X-V", "11:00-13:00");
        bbdd.insertSubject(3, "luis", "ibanez", "robotica", "luis@saul.com", "L-X", "11:00-13:00");
        bbdd.insertSubject(4, "tony", "ibanez", "teoria", "tony@saul.com", "J-V", "9:00-11:00");
        bbdd.insertSubject(5, "saul", "ibanez", "teoria", "saul@saul.com", "J-V", "9:00-11:00");

        int total = bbdd.getSubjects().size();
        assertTrue(total==6);
        assertTrue(bbdd.getSubject(2).getName().equals("pepe"));
        assertTrue(bbdd.getSubject(4).getSubject().equals("teoria"));
        bbdd.deleteSubject(3);
        total = bbdd.getSubjects().size();
        assertTrue(total==5);
        assertTrue(bbdd.getSubject(2)==null);

        for (int i = 0; i<6; i++){
            bbdd.deleteSubject(i);
        }
    }
}
