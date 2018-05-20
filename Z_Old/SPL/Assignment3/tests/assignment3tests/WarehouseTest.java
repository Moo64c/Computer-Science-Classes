package assignment3tests;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import assignment3.RepairMaterialInformation;
import assignment3.RepairToolInformation;
import assignment3.WarehouseImpl;
 
/**
 * Test class for Warehouse class.
 */

public class WarehouseTest {
    private WarehouseImpl warehouse;

	@Before
    public void setUp() throws Exception {
		// Create a warehouse.
        this.warehouse = new WarehouseImpl();
    }

    @After
    public void tearDown() throws Exception {
    	
    }

    @Test
    public void testAddRemoveTestItems() {
        // Add some materials and tools. Test the test-only implemented functions. 
        this.warehouse.addTestItems();
    	assertEquals(100, this.warehouse.materialAmount("Nails"));
    	assertEquals(30, this.warehouse.materialAmount("Shotgun Shells"));
    	assertEquals(3, this.warehouse.toolAmount("Hammer"));
    	assertEquals(1, this.warehouse.toolAmount("Shotgun"));
    	
    	this.warehouse.removeTestItems();
    	assertEquals(0, this.warehouse.materialAmount("Nails"));
    	assertEquals(0, this.warehouse.materialAmount("Shotgun Shells"));
    	assertEquals(0, this.warehouse.toolAmount("Hammer"));
    	assertEquals(0, this.warehouse.toolAmount("Shotgun"));
    }
    
    @Test
    public void testAcquireReleaseTool() {
    	this.warehouse.addTestItems();
    	
    	// Test acquireTool().
    	assertEquals(3, this.warehouse.toolAmount("Hammer"));
    	this.warehouse.acquireTool(new RepairToolInformation("Hammer", 1));
    	assertEquals(2, this.warehouse.toolAmount("Hammer"));
    	this.warehouse.acquireTool(new RepairToolInformation("Hammer", 1));
    	assertEquals(1, this.warehouse.toolAmount("Hammer"));
    	assertEquals(1, this.warehouse.toolAmount("Shotgun"));
    	this.warehouse.acquireTool(new RepairToolInformation("Shotgun", 1));
    	assertEquals(0, this.warehouse.toolAmount("Shotgun"));
    	
    	// Test releaseTool();
    	this.warehouse.releaseTool(new RepairToolInformation("Hammer", 1));
    	assertEquals(2, this.warehouse.toolAmount("Hammer"));
    	this.warehouse.releaseTool(new RepairToolInformation("Shotgun", 1));
    	assertEquals(1, this.warehouse.toolAmount("Shotgun"));
    	
    	this.warehouse.removeTestItems();
    	
    }
    
    @Test
    public void testConsumeMaterial() {
		this.warehouse.addTestItems();
    	assertEquals(this.warehouse.materialAmount("Nails"), 100);
		this.warehouse.consumeMaterial(new RepairMaterialInformation("Nails", 30));
		assertEquals(this.warehouse.materialAmount("Nails"), 70);
		this.warehouse.consumeMaterial(new RepairMaterialInformation("Nails", 30));
		assertEquals(this.warehouse.materialAmount("Nails"), 40);
		this.warehouse.consumeMaterial(new RepairMaterialInformation("Nails", 30));
		assertEquals(this.warehouse.materialAmount("Nails"), 10);
		this.warehouse.consumeMaterial(new RepairMaterialInformation("Nails", 5));
		assertEquals(this.warehouse.materialAmount("Nails"), 5);
		this.warehouse.consumeMaterial(new RepairMaterialInformation("Nails", 3));
		assertEquals(this.warehouse.materialAmount("Nails"), 2);
		
		assertEquals(this.warehouse.materialAmount("Shotgun Shells"), 30);
		this.warehouse.consumeMaterial(new RepairMaterialInformation("Shotgun Shells", 3));
		assertEquals(this.warehouse.materialAmount("Shotgun Shells"), 27);
		this.warehouse.consumeMaterial(new RepairMaterialInformation("Shotgun Shells", 22));
		assertEquals(this.warehouse.materialAmount("Shotgun Shells"), 5);
		this.warehouse.consumeMaterial(new RepairMaterialInformation("Shotgun Shells", 2));
		assertEquals(this.warehouse.materialAmount("Shotgun Shells"), 3);

		this.warehouse.removeTestItems();
    }
}
