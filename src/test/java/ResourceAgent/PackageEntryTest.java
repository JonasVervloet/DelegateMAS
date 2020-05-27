package ResourceAgent;

import ResourceAgent.PackageMap.PackageEntry;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PackageEntryTest {

    PackageEntry entry;

    @Before
    public void init() {
        entry = new PackageEntry(152, 5);
    }

    /*
    Constructor
     */
    @Test
    public void validConstructor() {
        assert(entry.matchesPackageId(152));
        assert(entry.hadEqualDistance(5));
        assertEquals(5, entry.getDistance());
        assert(entry.isEquaylEntry(152, 5));
        assertEquals(PackageEntry.getLifeTime(), entry.getTimeToLive());
    }

    @Test (expected = IllegalArgumentException.class)
    public void invalidPackageIdConstructor() {
        new PackageEntry(-1, 20);
    }

    @Test (expected = IllegalArgumentException.class)
    public void invalidDistanceConstructor() {
        new PackageEntry(258, -1);
    }

    /*
    Time to live
     */
    @Test
    public void testEvaporate() {
        assertEquals(PackageEntry.getLifeTime(), entry.getTimeToLive());
        entry.evaporate();
        assertEquals(PackageEntry.getLifeTime() - 1, entry.getTimeToLive());
        entry.evaporate();
        assertEquals(PackageEntry.getLifeTime() - 2, entry.getTimeToLive());
    }

    @Test
    public void testEvaporateJustBeforeFail() {
        for (int i = 0; i < PackageEntry.getLifeTime(); i++) {
            entry.evaporate();
        }
        assertEquals(0, entry.getTimeToLive());
    }

    @Test (expected = IllegalStateException.class)
    public void testEvaporateUntilFail() {
        for (int i = 0; i <= PackageEntry.getLifeTime(); i++) {
            entry.evaporate();
        }
    }

    @Test
    public void testRefresh() {
        entry.evaporate();
        entry.evaporate();
        entry.evaporate();
        assertEquals(PackageEntry.getLifeTime() - 3, entry.getTimeToLive());
        entry.refresh();
        assertEquals(PackageEntry.getLifeTime(), entry.getTimeToLive());
    }
}