import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeMap;

public class DB {

	// --------------------------------------------------------------------------------
	// Intance Variables
	// --------------------------------------------------------------------------------

	// Names-Addresses HashTable
	Hashtable<String, NEntry> namesHT;

	// Payments HashTables by Name
	Hashtable<String, ArrayList<PEntry>> paymentsNHT;

	// Payments HashTables by Invoices
	Hashtable<Integer, PEntry> paymentsIHT;

	// Expenses HashTable by Invoice
	Hashtable<Integer, ArrayList<EEntry>> expensesHT;

	// Expenses HashTable by Name
	Hashtable<String, Hashtable<Integer, ArrayList<EEntry>>> expensesNHT;

	// Invoice after Expenses TreeMap
	TreeMap<Integer, Integer> invoiceAfterExpenses;
	Graph users;

	// Initialize intance variables
	public DB() {
		namesHT = new Hashtable<>();
		paymentsNHT = new Hashtable<>();
		paymentsIHT = new Hashtable<>();
		expensesHT = new Hashtable<>();
		expensesNHT = new Hashtable<>();
		invoiceAfterExpenses = new TreeMap<>();
		users = new Graph(this);
	}

	// --------------------------------------------------------------------------------
	// Principal DataBase Methods
	// --------------------------------------------------------------------------------

	// Gets all the Name-HashTables Names with its
	public String getAllNames() {
		String output = "---------------------------- \n| Name |       Address      | \n---------------------------- \n";
		for (NEntry e : namesHT.values()) {
			output += "|" + e.toString() + "|\n";
		}
		output += "|\n---------------------------- \n";
		return output;

	}

	// Adds a new user. Its key is the name and the value is the address
	public void createNameEntry(String name, String address) {

		if (namesHT.containsKey(name)) {
			throw new IllegalArgumentException();
		}
		NEntry x = new NEntry(name, address);
		namesHT.put(name, x);
	}

	// Deletes an user
	public void deleteNameEntry(String name) {

		if (!namesHT.containsKey(name)) {
			throw new IllegalArgumentException();
		}

		if (!paymentsNHT.contains(name)) {
			namesHT.remove(name);
		} else {
			deleteAllPayments(name);
			namesHT.remove(name);
		}

	}

	// Restarts all the registered Payments
	private void deleteAllPayments(String name) {
		ArrayList<PEntry> x = selectFromPaymentsByN(name);

		Iterator<PEntry> it = x.iterator();

		while (it.hasNext()) {
			deletePaymentsEntry(it.next().invoice);
		}

	}

	// Returns an user entry by its Name as Key
	public NEntry selectFromNameByPK(String name) {
		if (!namesHT.containsKey(name)) {
			return null;
		}
		return namesHT.get(name);
	}

	public String selectName(String name) {
		return "---------------------------- \n| Name |       Address      | \n---------------------------- \n| "
				+ selectFromNameByPK(name).toString() + "|\n---------------------------- \n";
	}

	// Creates a Payment Entry, checks if the name is on the Names NameTable (if
	// not, throws an exception) and adds the payment registered on invoice
	// number (if there is not an invoice with that name allready)
	public void createPaymentsEntry(String name, Integer invoice, int payment) {

		if (!namesHT.containsKey(name) || paymentsIHT.containsKey(invoice)) {
			throw new IllegalArgumentException();
		}

		if (!paymentsNHT.containsKey(name)) {
			paymentsNHT.put(name, new ArrayList<>());
		}

		PEntry x = new PEntry(name, invoice, payment);
		// payments.add(x);
		paymentsNHT.get(name).add(x);
		paymentsIHT.put(invoice, x);
	}

	// Returns an String with all the payments formated by Name, Invoice and the
	// Payment
	public String getAllPayments() {
		String output = "---------------------------- \n| Name | Invoice | Payment |\n---------------------------- \n";
		for (PEntry e : paymentsIHT.values()) {
			output += "|" + e.toString() + "|\n";
		}
		output += "|\n---------------------------- \n";
		return output;

	}

	// Deletes a PaymentEntry by Invoice number
	public void deletePaymentsEntry(Integer invoice) {

		if (!paymentsIHT.containsKey(invoice)) {
			throw new IllegalArgumentException();
		}

		if (!expensesHT.contains(invoice)) {
			// payments.remove(paymentsIHT.get(invoice));
			paymentsNHT.remove(paymentsIHT.get(invoice).name);
			paymentsIHT.remove(invoice);
			invoiceAfterExpenses.remove(invoice);
		} else {
			System.out.println(":C");
			paymentsNHT.remove(paymentsIHT.get(invoice).name);
			paymentsIHT.remove(invoice);
			deleteExpenses(invoice);
			invoiceAfterExpenses.remove(invoice);
		}

	}

	// Deletes an Expense Register Invoice Number
	private void deleteExpenses(Integer invoice) {
		expensesHT.remove(invoice);
		String name = paymentsIHT.get(invoice).name;
		expensesNHT.get(name).remove(invoice);
		if (expensesNHT.get(name).isEmpty()) {
			expensesNHT.remove(name);
		}

	}

	// Returns a Payment Entry by Invoice Number
	public PEntry selectFromPaymentsByPK(Integer invoice) {
		if (!paymentsIHT.containsKey(invoice)) {
			return null;
		}
		return paymentsIHT.get(invoice);
	}

	// Returns all Payments Entries from an User
	public ArrayList<PEntry> selectFromPaymentsByN(String name) {
		if (!paymentsNHT.containsKey(name)) {
			return null;
		}
		return paymentsNHT.get(name);
	}

	public String selectPaymentByInvoice(Integer invoice) {
		return "---------------------------- \n| Name | Invoice | Payment |\n---------------------------- \n|"
				+ selectFromPaymentsByPK(invoice).toString() + "|\n---------------------------- \n";
	}

	public String selectPaymentsByName(String name) {
		String output = "---------------------------- \n| Name | Invoice | Payment |\n---------------------------- \n";
		ArrayList<PEntry> x = selectFromPaymentsByN(name);

		Iterator<PEntry> it = x.iterator();

		while (it.hasNext()) {
			output += "| " + it.next().toString() + " |\n";
		}

		output += "---------------------------- \n";

		return output;
	}

	// Registers an Expenses Entry by Invoice Number
	public void createExpensesEntry(Integer invoice, String item, Integer expense) {

		if (!paymentsIHT.containsKey(invoice)) {
			throw new IllegalArgumentException();
		}

		String name = paymentsIHT.get(invoice).name;

		if (!expensesNHT.containsKey(name)) {
			expensesNHT.put(name, new Hashtable<>());
		}

		if (!expensesNHT.get(name).containsKey(invoice)) {
			expensesNHT.get(name).put(invoice, new ArrayList<>());
		}

		if (!expensesHT.containsKey(invoice)) {
			expensesHT.put(invoice, new ArrayList<>());
		}

		EEntry x = new EEntry(invoice, item, expense);
		expensesHT.get(invoice).add(x);
		expensesNHT.get(name).get(invoice).add(x);
		invoiceAfterExpenses.put(invoice, afterExpenses(invoice));
	}

	// Returns all the expenses of the HashTable formated
	public String getAllExpenses() {
		String output = "---------------------------- \n| Invoice | item | Expense |\n---------------------------- \n";
		for (ArrayList<EEntry> a : expensesHT.values()) {
			Iterator<EEntry> it = a.iterator();
			while (it.hasNext()) {
				output += "|" + it.next().toString() + "|\n";
			}
		}
		output += "---------------------------- \n";
		return output;

	}

	// Returns a HastTable of an User (Name as Key) with an array of all its
	// Expenses
	public Hashtable<Integer, ArrayList<EEntry>> selectFromExpensesByN(String name) {
		if (!expensesNHT.containsKey(name)) {
			throw new IllegalArgumentException();
		}

		return expensesNHT.get(name);
	}

	// Returns an Array of all the Expenses of an Invoice Number
	public ArrayList<EEntry> selectFromExpensesByI(Integer invoice) {
		if (!expensesHT.containsKey(invoice)) {
			throw new IllegalArgumentException();
		}

		return expensesHT.get(invoice);
	}

	public String selectExpensesByInvoice(Integer invoice) {
		String output = "---------------------------- \n| Invoice | item | Expense |\n---------------------------- \n";
		ArrayList<EEntry> x = selectFromExpensesByI(invoice);

		Iterator<EEntry> it = x.iterator();

		while (it.hasNext()) {
			output += "| " + it.next().toString() + " |\n";
		}

		output += "---------------------------- \n";
		return output;
	}

	public String selectExpensesByName(String name) {
		String output = "---------------------------- \n| Name | item | Expense |\n---------------------------- \n";
		Hashtable<Integer, ArrayList<EEntry>> y = selectFromExpensesByN(name);

		for (ArrayList<EEntry> a : y.values()) {
			Iterator<EEntry> it = a.iterator();
			while (it.hasNext()) {
				output += "| " + it.next().toStringName() + " |\n";
			}
		}

		output += "---------------------------- \n";
		return output;
	}

	// Returns the sum of all the expenses
	private Integer totalExpenses(int invoice) {
		Integer expenses = 0;

		ArrayList<EEntry> x = selectFromExpensesByI(invoice);

		Iterator<EEntry> it = x.iterator();

		while (it.hasNext()) {
			expenses += it.next().expense;
		}

		return expenses;
	}

	// Returns the Substraction Payments minus Expenses
	private Integer afterExpenses(int invoice) {
		if (!paymentsIHT.containsKey(invoice)) {
			return null;
		}

		if (!expensesHT.containsKey(invoice)) {
			return selectFromPaymentsByPK(invoice).payment;
		}

		return selectFromPaymentsByPK(invoice).payment - totalExpenses(invoice);
	}

	// Formated afterExpenses method
	public String getEarningAfterExpenses(Integer invoice) {

		if (!paymentsIHT.containsKey(invoice)) {
			throw new IllegalArgumentException();
		}

		String output = "----------------------\n| Name | Invoice | Earning |\n----------------------\n";

		output += "| " + paymentsIHT.get(invoice).name + " | " + invoice + " | $" + invoiceAfterExpenses.get(invoice)
				+ " |\n----------------------\n";
		return output;
	}

	// Returns how similar are the expenses between two persons
	public int getHowSimilar(String name1, String name2) {
		int output = 0;

		ArrayList<PEntry> x = paymentsNHT.get(name1);
		ArrayList<PEntry> y = paymentsNHT.get(name2);

		Iterator<PEntry> itx = x.iterator();
		Iterator<PEntry> ity = y.iterator();

		while (itx.hasNext()) {
			output += totalExpenses(itx.next().invoice);
		}

		while (ity.hasNext()) {
			output -= totalExpenses(ity.next().invoice);
		}

		return Math.abs(output);
	}

	public int howSimilar(String name1, String name2) {
		if (paymentsNHT.containsKey(name1) && paymentsNHT.containsKey(name2)) {
			return users.getWeight(name1, name2);
		}
		throw new IllegalArgumentException();
	}

	// --------------------------------------------------------------------------------
	// Complementary Classes
	// --------------------------------------------------------------------------------

	// Name Entry (with its address) Class
	public class NEntry {

		String name, address;

		public NEntry(String n, String a) {
			name = n;
			address = a;
		}

		public String toString() {
			return name + " | " + address;
		}
	}

	// Payment Entry Class
	public class PEntry {

		String name;
		int payment;
		Integer invoice;

		public PEntry(String n, Integer i, int p) {
			name = n;
			invoice = i;
			payment = p;
		}

		public String toString() {
			return name + " | " + invoice + " | $" + payment;
		}
	}

	// Expense Entry Class
	public class EEntry {

		Integer invoice;
		int expense;
		String item;

		public EEntry(Integer i, String n, int e) {
			invoice = i;
			item = n;
			expense = e;
		}

		public String toString() {
			return invoice + " | " + item + " | $" + expense;
		}

		public String toStringName() {
			return paymentsIHT.get(invoice).name + " | " + item + " | $" + expense;
		}
	}

	// --------------------------------------------------------------------------------
	// Testing
	// --------------------------------------------------------------------------------
	public static void main(String[] args) {
		DB test = new DB();

		test.createNameEntry("Ana", "711-2880 Nulla St.");
		test.createNameEntry("Bruno", "606-3727 Ullamcorper. Street");
		test.createNameEntry("Carla", "Ap #867-859 Sit Rd.");
		test.createNameEntry("Dante", "935-9940 Tortor. Street");
		test.createNameEntry("Elsa", "5587 Nunc. Avenue");

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
		// test.deletePaymentsEntry(1001);

		System.out.println(test.getEarningAfterExpenses(1001));

		System.out.println(test.getHowSimilar("Ana", "Elsa"));
		System.out.println(test.howSimilar("Ana", "Elsa"));

	}

	// --------------------------------------------------------------------------------
	// @By Lucia Velasco & Adler Zamora -2016 Data Structure ITESM
	// --------------------------------------------------------------------------------
}
