package assignment3;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import assignment3.Customer.DAMAGE_TYPES;

public class Parser {
	
	public Parser() throws Exception {
		throw new Exception("static class");
	}
	
	/**
     * Creates a fully parsed Management object from the XML files
     * @param InitialDataFile - file that contain Warehouse and Clerk data
     * @param assetFile - file that contains data about Assets
     * @param assetContentFile - file that contains details every Asset type
     * @param customerFile - file that contains data about customers an rental requests.
     * @return a Management object with all the details. no change for management file is needed.
     */
    public static Management createManagement(String InitialDataFile, String assetFile, String assetContentFile, String customerFile)
    {
    	Management management = new Management();
    	parseInitialData(InitialDataFile, management);
    	parseAssetData(assetFile, assetContentFile, management);
    	parseCustomerData(customerFile, management);
    	
    	return management;
    	
    }
    
    
    /**
     * Parses data in CustomerGroups XML and adds the customer groups to management
     * @param fileName - customer group file to parse
     * @param management - management to modify
     */
    private static void parseCustomerData(String fileName, Management management){
    	try {
			Document doc = buildDocument(fileName);
			Element customerGroupElement = (Element) (doc.getElementsByTagName("CustomerGroups").item(0));
			for (Node group = customerGroupElement.getFirstChild(); group != null; group = group.getNextSibling())
			{
				if (group != null && group.getNodeType() == Node.ELEMENT_NODE && group.getNodeName() == "CustomerGroupDetails")
				{
					Element groupElement = (Element) group;
					management.addCustomerGroup(getCustomerGroup(groupElement));
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * parses customer group from XML
     * @param customerGroupElement - the customer group element
     * @return a new CustomerGroupDetails with data
     */
    private static CustomerGroupDetails getCustomerGroup (Element customerGroupElement){
    	CustomerGroupDetails ret = new CustomerGroupDetails(customerGroupElement.getElementsByTagName("GroupManagerName").item(0).getTextContent());
    	Node customers = customerGroupElement.getElementsByTagName("Customers").item(0);
    	for (Node customerNode = customers.getFirstChild(); customerNode != null; customerNode = customerNode.getNextSibling()) {
    		if (customerNode != null && customerNode.getNodeType() == Node.ELEMENT_NODE && customerNode.getNodeName() == "Customer")
    		{
    			Element customerElement = (Element) customerNode;
    			Customer.DAMAGE_TYPES vandlism_type = null;
    			switch(customerElement.getElementsByTagName("Vandalism").item(0).getTextContent()) {
	    			case "None":
		    			vandlism_type = DAMAGE_TYPES.NONE;
		    			break;
	    			case "Fixed":
	    				vandlism_type = DAMAGE_TYPES.FIXED;
	    				break;
	    			case "Arbitrary":
	    				vandlism_type = DAMAGE_TYPES.ARBITRARY;
	    				break;
    			}
    			
    			Customer customer = new Customer(customerElement.getElementsByTagName("Name").item(0).getTextContent(),
    					vandlism_type,
    					Double.parseDouble(customerElement.getElementsByTagName("MinimumDamage").item(0).getTextContent()),
    					Double.parseDouble(customerElement.getElementsByTagName("MaximumDamage").item(0).getTextContent()));
    			ret.addCustomer(customer);
    		}
    	}
    	
    	Node requests = customerGroupElement.getElementsByTagName("RentalRequests").item(0);
    	for (Node requestNode = requests.getFirstChild(); requestNode != null; requestNode = requestNode.getNextSibling()) {
    		if (requestNode != null && requestNode.getNodeType() == Node.ELEMENT_NODE && requestNode.getNodeName() == "Request")
    		{
    			Element requestElement = (Element) requestNode;
    			RentalRequest request = new RentalRequest(requestElement.getAttribute("id"),
    					requestElement.getElementsByTagName("Type").item(0).getTextContent(), 
    					Integer.parseInt(requestElement.getElementsByTagName("Size").item(0).getTextContent()),
    					Integer.parseInt(requestElement.getElementsByTagName("Duration").item(0).getTextContent()));
    			ret.addRentalRequest(request);
    		}
    	}
    	
    	return ret;
    }
    
    
    
    /**
     * parses data in Asset related files
     * @param assetFileName - the file that contains the assets in simulation
     * @param assetContentFileName - the file that contains asset content details.
     * @param management - the Mnagement object to modify.
     */
    private static void parseAssetData(String assetFileName, String assetContentFileName, Management management) {
    	Map<String, Vector<RepairToolInformation>> AssetContentTools = getToolMap(assetContentFileName);
    	Map<String, Vector<RepairMaterialInformation>> AssetContentMaterials = getMaterialMap(assetContentFileName);
    	
    	try {
			Document doc = buildDocument(assetFileName);
			Element assetsElement = (Element) doc.getElementsByTagName("Assets").item(0);
			for (Node asset = assetsElement.getFirstChild(); asset != null; asset = asset.getNextSibling())
			{
				if (asset != null && asset.getNodeType() == Node.ELEMENT_NODE && asset.getNodeName() == "Asset")
				{
					Element assetElement = (Element) asset;
					String name = assetElement.getElementsByTagName("Name").item(0).getTextContent();
					String type = assetElement.getElementsByTagName("Type").item(0).getTextContent();;
					int size = Integer.parseInt(assetElement.getElementsByTagName("Size").item(0).getTextContent());
					double costPerNight = Double.parseDouble(assetElement.getElementsByTagName("CostPerNight").item(0).getTextContent());
					Location loc = getLocation(assetElement);
					Map<String, AssetContent> content = getContentMap(assetElement, AssetContentTools, AssetContentMaterials);
					
					management.addAsset(name, type, loc, content, costPerNight, size);
					management.sortAssets();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }
    
    /**
     * parses the asset contents for an asset in the XML
     * @param parent - the parent asset Node.
     * @param tools - the vector that contains RepairToolInformation data for assets.
     * @param materials the vector that contains RepairMaterialInformation data for assets.
     * @return a map with the asset contents of a single asset
     */
    private static Map<String, AssetContent> getContentMap(Element parent, 
    		Map<String, Vector<RepairToolInformation>> tools, Map<String, Vector<RepairMaterialInformation>> materials){
    	
    	Map<String, AssetContent> ret = new TreeMap<String, AssetContent>();
    	Element assetContents = (Element) (parent.getElementsByTagName("AssetContents").item(0));
    	for (Node contentNode = assetContents.getFirstChild(); contentNode != null; contentNode = contentNode.getNextSibling())
    	{
    		if (contentNode != null && contentNode.getNodeType() == Node.ELEMENT_NODE && contentNode.getNodeName() == "AssetContent")
    		{
    			Element contentElement = (Element) contentNode;
    			
    			String contentName = contentElement.getElementsByTagName("Name").item(0).getTextContent();
    			double contentRepairMultiplier = Double.parseDouble(contentElement.getElementsByTagName("RepairMultiplier").item(0).getTextContent());
    			
    			AssetContent newContent = new AssetContent(contentName, contentRepairMultiplier);
    			
    			Vector<RepairToolInformation> contentTools = tools.get(contentName);  			
    			for (int i = 0; i < contentTools.size(); i++)
    				newContent.addRepairToolInfo(contentTools.get(i));
    			
    			Vector<RepairMaterialInformation> contentMaterials = materials.get(contentName);  			
    			for (int i = 0; i < contentMaterials.size(); i++)
    				newContent.addRepairMaterialInfo(contentMaterials.get(i));
    			
    			ret.put(contentName, newContent);
    		}
    	}
    	
    	return ret;
    }
    
    /**
     * parses the RepairToolInformation for every asset
     * @param fileName - XML file with the info. 
     * @return a map with a RepairToolInformation Vector per each AssetContent (key is name).  
     */
    private static Map<String, Vector<RepairToolInformation>> getToolMap(String fileName) {

    	Map<String, Vector<RepairToolInformation>> ret = new TreeMap<String, Vector<RepairToolInformation>>();
    	
    	try {
			Document doc = buildDocument(fileName);
			
			Node AssetContentNode = doc.getElementsByTagName("AssetContentsRepairDetails").item(0);
			
			for (Node content = AssetContentNode.getFirstChild(); content != null; content = content.getNextSibling())
			{
				if (content != null && content.getNodeType() == Node.ELEMENT_NODE && content.getNodeName() == "AssetContent")
				{
					Element elementContent = (Element) content;
					String AssetContentName = elementContent.getElementsByTagName("Name").item(0).getTextContent();
					Vector<RepairToolInformation> vec = new Vector<RepairToolInformation>();
					fillAssetContentTools(elementContent, vec);
					
					ret.put(AssetContentName, vec);
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return ret;
    }
    
    /**
     * auxiliary function that adds RepairToolInformation items from the AssetContent xml to a vector.
     * @param content - the "Tools" content Element, that contain tool sub-elements.
     * @param vec - vector to add to
     */
    private static void fillAssetContentTools(Element content, Vector<RepairToolInformation> vec){
    	Element tools = (Element) (content.getElementsByTagName("Tools").item(0));

    	for (Node tool = tools.getFirstChild(); tool != null; tool = tool.getNextSibling())
    	{
    		if (tool != null && tool.getNodeType() == Node.ELEMENT_NODE && tool.getNodeName() == "Tool")
    		{
    			Element toolElement = (Element) tool;
    			RepairToolInformation repairToolInfo = new RepairToolInformation(toolElement.getElementsByTagName("Name").item(0).getTextContent(),
    					Integer.parseInt(toolElement.getElementsByTagName("Quantity").item(0).getTextContent()));
    			vec.add(repairToolInfo);

    		}
    	}  	
    }
    
    
    /**
     * parses the RepairMaterialInformation for every asset
     * @param fileName - XML file with the info. 
     * @return a map with a RepairMaterialInformation Vector per each AssetContent (key is name).  
     */
    private static Map<String, Vector<RepairMaterialInformation>> getMaterialMap(String fileName) {

    	Map<String, Vector<RepairMaterialInformation>> ret = new TreeMap<String, Vector<RepairMaterialInformation>>();
    	
    	try {
			Document doc = buildDocument(fileName);
			
			Node AssetContentNode = doc.getElementsByTagName("AssetContentsRepairDetails").item(0);
			
			for (Node content = AssetContentNode.getFirstChild(); content != null; content = content.getNextSibling())
			{
				if (content != null && content.getNodeType() == Node.ELEMENT_NODE && content.getNodeName() == "AssetContent")
				{
					Element elementContent = (Element) content;
					String AssetContentName = elementContent.getElementsByTagName("Name").item(0).getTextContent();
					Vector<RepairMaterialInformation> vec = new Vector<RepairMaterialInformation>();
					fillAssetContentMaterials(elementContent, vec);
					
					ret.put(AssetContentName, vec);
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return ret;
    }
    
    /**
     * auxiliary function that adds RepairToolInformation items from the AssetContent xml to a vector.
     * @param content - the "Tools" content Element, that contain tool sub-elements.
     * @param vec - vector to add to
     */
    private static void fillAssetContentMaterials(Element content, Vector<RepairMaterialInformation> vec){
    	Element materials = (Element) (content.getElementsByTagName("Materials").item(0));
    	
    	for (Node material = materials.getFirstChild(); material != null; material = material.getNextSibling())
    	{
    		if (material != null && material.getNodeType() == Node.ELEMENT_NODE && material.getNodeName() == "Material")
    		{
    			Element materialElement = (Element) material;
    			RepairMaterialInformation repairMaterialInfo = new RepairMaterialInformation(materialElement.getElementsByTagName("Name").item(0).getTextContent(),
    					Integer.parseInt(materialElement.getElementsByTagName("Quantity").item(0).getTextContent()));
    			vec.add(repairMaterialInfo);
    		}
    	}
    }
    
    /**
     * parses the warehouse content and clerks data from InitialData XML
     * @param fileName - the file to parse
     * @param management - the Management object to modify.
     */
    private static void parseInitialData(String fileName, Management management) {
    	
		try {
			
			Document doc = buildDocument(fileName);
			Node warehouseNode = doc.getElementsByTagName("Warehouse").item(0);
			
			if (warehouseNode.getNodeType() == Node.ELEMENT_NODE && warehouseNode.getNodeName() == "Warehouse")
			{
				Element warehouseElement = (Element) warehouseNode;
				Node tools = warehouseElement.getElementsByTagName("Tools").item(0);
				for (Node tool = tools.getFirstChild(); tool != null; tool = tool.getNextSibling()) {
					if (tool != null && tool.getNodeType() == Node.ELEMENT_NODE && tool.getNodeName() == "Tool")
					{
						addTool(tool, management);
					}
				}
				
				Node materials = warehouseElement.getElementsByTagName("Materials").item(0);

				for (Node material = materials.getFirstChild(); material != null; material = material.getNextSibling()) {
					if (material != null && material.getNodeType() == Node.ELEMENT_NODE && material.getNodeName() == "Material")
					{
						addMaterial(material, management);
					}
				}
			}
			
			Node staffNode = doc.getElementsByTagName("Staff").item(0);
			if (warehouseNode.getNodeType() == Node.ELEMENT_NODE && warehouseNode.getNodeName() == "Warehouse"){
				
				Element staffElement = (Element) staffNode;
				
				Element repairmen = (Element) (staffElement.getElementsByTagName("NumberOfMaintenancePersons").item(0));
				management.setNumberOfRepairmen(Integer.parseInt(repairmen.getTextContent()));
				
				Element numOfRequests = (Element) (staffElement.getElementsByTagName("TotalNumberOfRentalRequests").item(0));
				management.setNumberOfRequestsNotHandled(Integer.parseInt(numOfRequests.getTextContent()));
				
				Node clerks = staffElement.getElementsByTagName("Clerks").item(0);
				getClerks(clerks, management);
					
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

    }

    
    /**
     * Auxiliary function, builds the XML document from filename.
     * @param fileName - to build XML from.
     * @return Document object
     */
    private static Document buildDocument(String fileName) throws Exception {
    	File fXmlFile = new File(fileName);
    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    	DocumentBuilder dBuilder;
		dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		
		return doc;
    }
    
    /**
     * Auxiliary function, adds tools to management's warehouse
     * @param tool - the tool node to parse and add.
     * @param management - the management to add to.
     */
    private static void addTool(Node tool, Management management){
		Element toolElement = (Element) tool;

		management.addItemRepairTool(toolElement.getElementsByTagName("Name").item(0).getTextContent(),
				Integer.parseInt(toolElement.getElementsByTagName("Quantity").item(0).getTextContent()));

    }

    /**
     * Auxiliary function, adds tools to management's warehouse
     * @param material - the material node to parse and add.
     * @param management - the management to add to.
     */
    private static void addMaterial(Node material, Management management){
		Element materialElement = (Element) material;

		management.addItemRepairMaterial(materialElement.getElementsByTagName("Name").item(0).getTextContent(),
				Integer.parseInt(materialElement.getElementsByTagName("Quantity").item(0).getTextContent()));

    }

    /**
     * Auxiliary function, adds tools to management's warehouse
     * @param clerks - the clerks node to parse and add.
     * @param management - the management to add to.
     */
    private static void getClerks(Node clerks, Management management){
    	for (Node clerk = clerks.getFirstChild(); clerk != null; clerk = clerk.getNextSibling()){
			if (clerk != null && clerk.getNodeType() == Node.ELEMENT_NODE && clerk.getNodeName() == "Clerk") {
								
				Element clerkElement = (Element) clerk;
								
				Location clerkLocation = getLocation(clerkElement);
			
				management.addClerk(clerkElement.getElementsByTagName("Name").item(0).getTextContent(),
					clerkLocation);
			}
		}
    }
    
    
    /**
     * auxiliary function to retrieve location.
     * @param locationParent - the parent element that has a location tag
     * @return Location object of the parent element.
     */
    private static Location getLocation(Element locationParent){
		NamedNodeMap locationAttributes = locationParent.getElementsByTagName("Location").item(0).getAttributes();
		
		Location location = new Location (Integer.parseInt(locationAttributes.getNamedItem("x").getNodeValue()),
				Integer.parseInt(locationAttributes.getNamedItem("y").getNodeValue()));
		
		return location;

    }

}
