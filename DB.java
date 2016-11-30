import java.util.ArrayList;
import java.util.Hashtable;

public class DB {
	
	Hashtable<String, NEntry> namesHT; 
	
	//ArrayList<PEntry> payments;
	Hashtable<String, ArrayList<PEntry>> paymentsNHT;
	Hashtable<Integer, PEntry> paymentsIHT;
	
	ArrayList<EEntry> expenses;
	Hashtable<Integer, ArrayList<EEntry>> expensesHT;
	Hashtable<String, Hashtable<Integer, ArrayList<EEntry>>> expensesNHT;
	
	
	
	public DB(){
		expenses = new ArrayList<>();
		namesHT = new Hashtable<>(); 
		paymentsNHT = new Hashtable<>();
		paymentsIHT = new Hashtable<>();
		expensesHT = new Hashtable<>();
		expensesNHT = new Hashtable<>();
		
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
	}
	
	/*public void deleteExpensesEntry(Integer invoice, String item){
		if(!expensesInvHT.containsKey(invoice) || !expensesInvHT.get(invoice).contains(item)){
			throw new IllegalArgumentException();
		} 
		
		expenses.remove(expensesInvHT.get(invoice).get(item));
		expensesNHT.remove(invoice);
		expensesInvHT.get(invoice).remove(item);
	}
	
	public EEntry selectFromExpensesByPK(Integer invoice, String item){
		if(!expensesInvHT.containsKey(invoice) || !expensesInvHT.get(invoice).contains(item)){
			throw new IllegalArgumentException();
		} 
		
		 return expensesInvHT.get(invoice).get(item);
	}*/
	
	public Hashtable<Integer, ArrayList<EEntry>> selectFromExpensesByN(String name){
		if(!expensesNHT.containsKey(name)){
			throw new IllegalArgumentException();
		}
		
		return expensesNHT.get(name);
	}
	
	private int afterExpenses(String name, int invoice){
		if(!namesHT.contains(name)){
			throw new IllegalArgumentException();
		}
		
		return 888;
	}
	
	public class NEntry{
		
		String name, address;
		
		public NEntry(String n, String a){
			name = n;
			address = a;
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

		ArrayList<PEntry> b = test.selectFromPaymentsByN("Ana");
		
	
		
		
		System.out.println(test.selectFromNameByPK("Ana"));

		

		
		
		
	}
	
}
