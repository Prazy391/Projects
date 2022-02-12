/**
 HW3.java: Simple driver to test Money, Date, and Bill classes
 @author Rob Nash, borrowed from cfolsen
 */
public class Driver
{

    /**
     main driver function
     pre:  none
     post: exercises the methods in Bill, Money, and Date (not done)
     */
    public static void main(String[] args)
    {
        //Construct some money
        Money money1 = new Money(10);
        Money money2 = money1;
        money1.setMoney(30,50);
        money2.add(30);
        
        //TODO: do more functional exercises with the money class
	    
	    
        System.out.println("Money objects output:");
        System.out.println(money1.toString());
        System.out.println(money2.equals(money1));
	
	
        //Construct some bills
        Money amount = new Money(20);
        Date dueDate = new Date(4, 30, 2014);
        Bill bill1 = new Bill(amount, dueDate, "The phone company");
       
        Bill bill2 = bill1;
        bill2.setDueDate(new Date(5, 30, 2020));
        amount.setMoney(31, 99);
        dueDate.setDay(29);
        Bill bill3 = new Bill(amount, dueDate, "The record company");
        bill3.setPaid(dueDate);
        
        System.out.println("Bill objects output:");
        System.out.println(bill1);
        System.out.println(bill2);
        System.out.println(bill3);
        System.out.println("Bill objects output:");
        //Adding bills to a expense account
        ExpenseAccount myAccount = new ExpenseAccount();
        myAccount.insert(bill1,0);
        myAccount.insert(bill2,1);
        myAccount.insert(bill3,8);
        System.out.println(myAccount.toString());
        //Testing comparables
        System.out.println(bill1.compareTo(bill3));
        System.out.println(bill1.getAmount().compareTo(bill3.getAmount()));
        System.out.println(bill1.getDueDate().compareTo(bill3.getDueDate()));
    }
}
