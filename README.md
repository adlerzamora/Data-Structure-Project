$Data-Structure-Project
========

$In this final project you are asked to design a basic database with tree tables:
--------
    - A table of names with addresses (Table 1).
    - A table of invoices (Table 2).
    - A table of expenses (Table 3).

Features
--------

- Insert the different quantities in the different tables by primary id.
- Delete quantities by their primary id at each table.
- Select names from the first table and see their total expenses.
- Select names from the first table and see their total payments
- Return the earning after expenses in a similar fashion.
- Calculate how similar are different users by expense.

Installation
------------

Install $Data-Structure-Project by running:

    git clone https://github.com/adlerzamora/Data-Structure-Project.git

Methods
----------
- Adds a new user. Its key is the name and the value is the address:

	public void createNameEntry(String name, String address) {

		if (namesHT.containsKey(name)) {
			throw new IllegalArgumentException();
		}
		NEntry x = new NEntry(name, address);
		namesHT.put(name, x);
	}

- Deletes an user:

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

- Restarts all the registered Payments:

	private void deleteAllPayments(String name) {
		ArrayList<PEntry> x = selectFromPaymentsByN(name);

		Iterator<PEntry> it = x.iterator();

		while (it.hasNext()) {
			deletePaymentsEntry(it.next().invoice);
		}

	} 

- Returns an user entry by its Name as Key:

	public NEntry selectFromNameByPK(String name) {
		if (!namesHT.containsKey(name)) {
			return null;
		}
		return namesHT.get(name);
	}

- Creates a Payment Entry, checks if the name is on the Names NameTable (if not, throws an exception) and adds the payment 
  registered on invoice number (if there is not an invoice with that name allready):

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

- Deletes a PaymentEntry by Invoice number:

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

- Deletes an Expense Register Invoice Number:

	private void deleteExpenses(Integer invoice) {
		expensesHT.remove(invoice);
		String name = paymentsIHT.get(invoice).name;
		expensesNHT.get(name).remove(invoice);
		if (expensesNHT.get(name).isEmpty()) {
			expensesNHT.remove(name);
		}
	}

- Returns a Payment Entry by Invoice Number:

	public PEntry selectFromPaymentsByPK(Integer invoice) {
		if (!paymentsIHT.containsKey(invoice)) {
			return null;
		}
		return paymentsIHT.get(invoice);
	}

- Returns all Payments Entries from an User:

	public ArrayList<PEntry> selectFromPaymentsByN(String name) {
		if (!paymentsNHT.containsKey(name)) {
			return null;
		}
		return paymentsNHT.get(name);
	}

- Registers an Expenses Entry by Invoice Number:

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

- Returns a HastTable of an User (Name as Key) with an array of all its expenses:

	public Hashtable<Integer, ArrayList<EEntry>> selectFromExpensesByN(String name) {
		if (!expensesNHT.containsKey(name)) {
			throw new IllegalArgumentException();
		}
		return expensesNHT.get(name);
	}

- Returns an Array of all the Expenses of an Invoice Number:

	public ArrayList<EEntry> selectFromExpensesByI(Integer invoice) {
		if (!expensesHT.containsKey(invoice)) {
			throw new IllegalArgumentException();
		}

		return expensesHT.get(invoice);
	}

- Returns the sum of all the expenses:

	private Integer totalExpenses(int invoice) {
		Integer expenses = 0;

		ArrayList<EEntry> x = selectFromExpensesByI(invoice);

		Iterator<EEntry> it = x.iterator();

		while (it.hasNext()) {
			expenses += it.next().expense;
		}

		return expenses;
	}

- Returns the Substraction Payments minus Expenses: 

	private Integer afterExpenses(int invoice) {
		if (!paymentsIHT.containsKey(invoice)) {
			return null;
		}

		if (!expensesHT.containsKey(invoice)) {
			return selectFromPaymentsByPK(invoice).payment;
		}

		return selectFromPaymentsByPK(invoice).payment - totalExpenses(invoice);
	}

- Returns how similar are the expenses between two persons:

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

Contribute
----------

- Source Code: https://github.com/adlerzamora/Data-Structure-Project

Support
-------

If you are having issues, please let us know.
We have a mailing list located at: adlerzamora@gmail.com

License
-------
Pending

