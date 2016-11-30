import java.util.ArrayList;
import java.util.Hashtable;

public class DB {
	
	Hashtable<String, NEntry> namesHT; 
	
	//ArrayList<PEntry> payments;
	Hashtable<String, ArrayList<PEntry>> paymentsNHT;
	Hashtable<Integer, PEntry> paymentsIHT;
	
	ArrayList<EEntry> expenses;
	Hashtable<String, ArrayList<EEntry>> expensesNHT;
	Hashtable<Integer, Hashtable<String, EEntry>> expensesInvHT;
	
	
	
	public DB(){
		//payments  = new ArrayList<>();
		expenses = new ArrayList<>();
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
		
		if(!expensesInvHT.contains(invoice)){
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
	
	public ArrayList<PEntry> selectNP(String name){
		if(!paymentsNHT.containsKey(name)){
			return null;
		} 
		 return paymentsNHT.get(name);
	}
	
	public void createExpensesEntry(Integer invoice, String item, Integer expense){
		
		if(paymentsIHT.containsKey(invoice) || (expensesInvHT.contains(invoice) && expensesInvHT.get(invoice).contains(item))){
			throw new IllegalArgumentException();
		} 
		String name = paymentsIHT.get(invoice).name;
		if(!expensesNHT.containsKey(name)){
			paymentsNHT.put(name, new ArrayList<>());
		}
		
		if(!expensesInvHT.contains(invoice)){
			expensesInvHT.put(invoice, new Hashtable<String, EEntry>());
		}
		
		EEntry x = new EEntry(invoice, item, expense);
		expenses.add(x);
		expensesNHT.get(name).add(x);
		expensesInvHT.get(invoice).put(item, x);
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
	
	public ArrayList<EEntry> selectNE(String name){
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
		
	}
	
}
