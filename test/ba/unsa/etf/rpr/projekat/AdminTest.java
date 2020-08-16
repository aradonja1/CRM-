package ba.unsa.etf.rpr.projekat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminTest {

    @Test
    public void constructor() {
        Admin admin = new Admin();
        //there isn't exception
        assertTrue(true);
        admin = new Admin(2, "Admin", "admin", "admin", "admin");
        assertEquals("admin", admin.getUsername());
    }
}