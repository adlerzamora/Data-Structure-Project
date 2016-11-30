import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeMap;

public class DB {
	
	Hashtable<String, NEntry> namesHT; 
	
	//ArrayList<PEntry> payments;
	Hashtable<String, ArrayList<PEntry>> paymentsNHT;
	Hashtable<Integer, PEntry> paymentsIHT;
	
	ArrayList<EEntry> expenses;
	Hashtable<Integer, ArrayList<EEntry>> expensesHT;
	Hashtable<String, Hashtable<Integer, ArrayList<EEntry>>> expensesNHT;
	TreeMap<Integer, Integer> invoiceAfterExpenses;
	
	
	
	public DB(){
		expenses = new ArrayList<>();
		namesHT = new Hashtable<>(); 
		paymentsNHT = new Hashtable<>();
		paymentsIHT = new Hashtable<>();
		expensesHT = new Hashtable<>();
		expensesNHT = new Hashtable<>();
		invoiceAfterExpenses = new TreeMap<>();
		
	}
	
	public String getAllNames(){
		String output = "---------------------------- \n| Name |       Adress      | \n---------------------------- \n";
		for(NEntry e: namesHT.values()){
			output += "|"+e.toString()+"|\n";
		}
		output+="|\n---------------------------- \n";
		return output;

	}
	
	public void createNameEntry(String name, String address){
		
		if(namesHT.containsKey(name)){
			throw new IllegalArgumentException();
		} 
		NEntry x = new NEntry(name, address); 
		namesHT.put(name, x);		
	}
	
	public void deleteNameEntry(String name){
		
		if(!namesHT.containsKey(name)){
			throw new IllegalArgumentException();
		} 
		
		if(!paymentsNHT.contains(name)){
			namesHT.remove(name);
		} else {
			System.out.println(":C");
		}
		
	}
	
	public NEntry selectFromNameByPK(String name){
		if(!namesHT.containsKey(name)){
			return null;
		} 
		 return namesHT.get(name);
	}
	
	public String selectName(String name){
		return "---------------------------- \n| Name |       Adress      | \n---------------------------- \n| "+selectFromNameByPK(name).toString()+"|\n---------------------------- \n";
	}
	
	public void createPaymentsEntry(String name, Integer invoice, int payment){
		
		if(!namesHT.containsKey(name) || paymentsIHT.containsKey(invoice)){
			throw new IllegalArgumentException();
		} 
		
		if(!paymentsNHT.containsKey(name)){
			paymentsNHT.put(name, new ArrayList<>());
		}
		
		PEntry x = new PEntry(name, invoice, payment);
		//payments.add(x);
		paymentsNHT.get(name).add(x);
		paymentsIHT.put(invoice, x);
	}
	
	public String getAllPayments(){
		String output = "---------------------------- \n| Name | Invoice | Payment |\n---------------------------- \n";
		for(PEntry e: paymentsIHT.values()){
			output += "|"+e.toString()+"|\n";
		}
		output+="|\n---------------------------- \n";
		return output;

	}
	
	public void deletePaymentsEntry(Integer invoice){
		
		if(!paymentsIHT.containsKey(invoice)){
			throw new IllegalArgumentException();
		} 
		
		if(!expensesHT.contains(invoice)){
			//payments.remove(paymentsIHT.get(invoice));
			paymentsNHT.remove(paymentsIHT.get(invoice).name);
			paymentsIHT.remove(invoice);
		} else {
			System.out.println(":C");
		}
		
	}
	
	public PEntry selectFromPaymentsByPK(Integer invoice){
		if(!paymentsIHT.containsKey(invoice)){
			return null;
		} 
		 return paymentsIHT.get(invoice);
	}
	
	public ArrayList<PEntry> selectFromPaymentsByN(String name){
		if(!paymentsNHT.containsKey(name)){
			return null;
		} 
		 return paymentsNHT.get(name);
	}
	public String selectPaymentByInvoice(Integer invoice){
		return "---------------------------- \n| Name | Invoice | Payment |\n---------------------------- \n|"+selectFromPaymentsByPK(invoice).toString()+"|\n---------------------------- \n";
	}
	
	public String selectPaymentsByName(String name){
		String output = "---------------------------- \n| Name | Invoice | Payment |\n---------------------------- \n";
		ArrayList<PEntry> x = selectFromPaymentsByN(name);
		
		Iterator it = x.iterator();
		
		while(it.hasNext()){
			output += "| "+it.next().toString()+" |\n";
		}
		
		output += "---------------------------- \n";

		
		return output;
	}
	
	public void createExpensesEntry(Integer invoice, String item, Integer expense){
		
		if(!paymentsIHT.containsKey(invoice)){
			throw new IllegalArgumentException();
		}
		
		String name = paymentsIHT.get(invoice).name;
		
		if(!expensesNHT.containsKey(name)){
			expensesNHT.put(name, new Hashtable<>());
		}
		
		if(!expensesNHT.get(name).containsKey(invoice)){
			expensesNHT.get(name).put(invoice, new ArrayList<>());
		}
		
		if(!expensesHT.containsKey(invoice)){
			expensesHT.put(invoice, new ArrayList<>());
		}
		
		EEntry x = new EEntry(invoice, item, expense);
		expenses.add(x);
		expensesHT.get(invoice).add(x);
		expensesNHT.get(name).get(invoice).add(x);
		expensesIT.put(invoice, name);
	}
	
	public String getAllExpenses(){
		String output = "---------------------------- \n| Invoice | item | Expense |\n---------------------------- \n";
		for(ArrayList<EEntry> a: expensesHT.values()){
			Iterator<EEntry> it = a.iterator();
			while(it.hasNext()){
				output += "|"+it.next().toString()+"|\n";
			}
		}
		output+="---------------------------- \n";
		return output;

	}
	
	public Hashtable<Integer, ArrayList<EEntry>> selectFromExpensesByN(String name){
		if(!expensesNHT.containsKey(name)){
			throw new IllegalArgumentException();
		}
		
		return expensesNHT.get(name);
	}
	
	public ArrayList<EEntry> selectFromExpensesByI(Integer invoice){
		if(!expensesHT.containsKey(invoice)){
			throw new IllegalArgumentException();
		}
		
		return expensesHT.get(invoice);
	}
	
	public String selectExpensesByInvoice(Integer invoice){
		String output = "---------------------------- \n| Invoice | item | Expense |\n---------------------------- \n";
		ArrayList<EEntry> x = selectFromExpensesByI(invoice);
		
		Iterator<EEntry> it = x.iterator();
		
		while(it.hasNext()){
			output += "| "+it.next().toString()+" |\n";
		}
		
		output += "---------------------------- \n";
		return output;
	}
	
	public String selectExpensesByName(String name){
		String output = "---------------------------- \n| Name | item | Expense |\n---------------------------- \n";
		Hashtable<Integer, ArrayList<EEntry>> y = selectFromExpensesByN(name);
		
		for(ArrayList<EEntry> a: y.values()){
			Iterator<EEntry> it = a.iterator();
			while(it.hasNext()){
				output += "| "+it.next().toStringName()+" |\n";
			}
		}
		
		output += "---------------------------- \n";
		return output;
	}
	
	private Integer afterExpenses(int invoice){
		if(!paymentsIHT.containsKey(invoice)){
			return null;
		} 
		Integer payment = selectFromPaymentsByPK(invoice).payment;
		Integer expenses = 0;
		
		ArrayList<EEntry> x = selectFromExpensesByI(invoice);
		
		Iterator<EEntry> it = x.iterator();
		
		while(it.hasNext()){
			expenses += it.next().expense;
		}
		
		return payment-expenses;
	}
	
	public class NEntry{
		
		String name, address;
		
		public NEntry(String n, String a){
			name = n;
			address = a;
		}
		
		public String toString(){
			return name + " | " + address;
		}
	}
	
	public class PEntry{
		
		String name;
		int  payment;
		Integer invoice;
		
		public PEntry(String n, Integer i, int p){
			name = n;
			invoice = i;
			payment = p;
		}
		
		public String toString(){
			return name + " | " + invoice + " | $" + payment;
		}
	}
	
	public class EEntry{
		
		Integer invoice;
		int expense;
		String item;
		
		public EEntry(Integer i, String n, int e){
			invoice = i;
			item = n;
			expense = e; 
		}
		
		public String toString(){
			return invoice + " | " + item + " | $" + expense;
		}
		
		public String toStringName(){
			return paymentsIHT.get(invoice).name + " | " + item + " | $" + expense;
		}
	}

	public static void main(String[] args) {
		DB test = new DB();
		
		test.createNameEntry("Ana","711-2880 Nulla St.");
		test.createNameEntry("Bruno","606-3727 Ullamcorper. Street");
		test.createNameEntry("Carla","Ap #867-859 Sit Rd.");
		test.createNameEntry("Dante","935-9940 Tortor. Street");
		test.createNameEntry("Elsa","5587 Nunc. Avenue");
		
		test.createPaymentsEntry("Ana", 1001, 1000);
		test.createPaymentsEntry("Bruno", 1002, 2000);
		test.createPaymentsEntry("Carla", 1003, 2500);
		test.createPaymentsEntry("Dante", 1004, 3000);
		test.createPaymentsEntry("Elsa", 1005, 3100);
		test.createPaymentsEntry("Ana", 1006, 5000);
		test.createPaymentsEntry("Bruno", 1007, 1000);
		test.createPaymentsEntry("Carla", 1008, 2000);
		test.createPaymentsEntry("Dante", 1009, 2500);
		test.createPaymentsEntry("Elsa", 1010, 3000);

		test.createExpensesEntry(1001, "Beer", 10);
		test.createExpensesEntry(1002, "Food", 30);
		test.createExpensesEntry(1003, "Uber", 50);
		test.createExpensesEntry(1004, "Beer", 70);
		test.createExpensesEntry(1005, "Food", 90);
		test.createExpensesEntry(1006, "Uber", 110);
		test.createExpensesEntry(1007, "Beer", 130);
		test.createExpensesEntry(1008, "Food", 150);
		test.createExpensesEntry(1009, "Uber", 170);
		test.createExpensesEntry(1010, "Beer", 190);
		test.createExpensesEntry(1001, "Food", 210);
		test.createExpensesEntry(1002, "Uber", 230);
		test.createExpensesEntry(1003, "Beer", 250);
		test.createExpensesEntry(1004, "Food", 270);
		test.createExpensesEntry(1005, "Uber", 290);
		test.createExpensesEntry(1006, "Beer", 310);
		test.createExpensesEntry(1007, "Food", 330);
		test.createExpensesEntry(1008, "Uber", 350);
		test.createExpensesEntry(1009, "Beer", 370);
		test.createExpensesEntry(1010, "Food", 390);
		
		System.out.println(test.selectName("Ana"));
		System.out.println(test.getAllNames());
		
		System.out.println(test.getAllPayments());
		System.out.println(test.selectPaymentByInvoice(1001));
		System.out.println(test.selectPaymentsByName("Ana"));

		System.out.println(test.getAllExpenses());
		System.out.println(test.selectExpensesByInvoice(1001));
		System.out.println(test.selectExpensesByName("Ana"));

	}
	
}
