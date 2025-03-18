package Practice.ATM;

import java.util.Date;

public class ATMSystem {
    public static void main(String[] args) {
        ATMRoom atmRoom = new ATMRoom();
        atmRoom.atm.printCurrentATMStatus();
        atmRoom.atm.getAtmState().insertCard(atmRoom.atm, atmRoom.user.getCard());
        atmRoom.atm.getAtmState().authenticatePin(atmRoom.atm, atmRoom.user.getCard(), 112211);
        atmRoom.atm.getAtmState().selectOperation(atmRoom.atm, atmRoom.user.getCard(), TransactionType.CASH_WITHDRAWAL);
        atmRoom.atm.getAtmState().cashWithdrawal(atmRoom.atm, atmRoom.user.getCard(), 2700);
        atmRoom.atm.printCurrentATMStatus();

    }
}

class ATMRoom {
    ATM atm;
    User user;
    public ATMRoom() {
        initialize();
    }
    private void initialize() {
        atm = ATM.getATMObj();
        atm.setATMBalance(3500, 1,2,5);
        this.user = createUser();
    }

    private User createUser(){
        return new User(createCard());
    }

    private Card createCard(){
        return new Card(createBankAccount());
    }

    private BankAccount createBankAccount() {
        return new BankAccount(3000);

    }

}

class ATM {
    private static ATM atm = new ATM();
    private ATMState atmState;
    private int balance;
    private int cnt2kNotes;
    private int cnt500Notes;
    private int cnt100Notes;

    public ATM() {
    }

    public static ATM getATMObj() {
        atm.setAtmState(new IdleState());
        return atm;
    }

    public ATMState getAtmState() {
        return atmState;
    }

    public int getBalance() {
        return balance;
    }

    public int getCnt2kNotes() {
        return cnt2kNotes;
    }

    public int getCnt500Notes() {
        return cnt500Notes;
    }

    public int getCnt100Notes() {
        return cnt100Notes;
    }

    public void setAtmState(ATMState atmState) {
        this.atmState = atmState;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void deductBalance(int amount) {
        balance -= amount;
    }

    public void deduct2kNotes(int cnt2kNotes) {
        this.cnt2kNotes -= cnt2kNotes;
    }
    public void deduct500Notes(int cnt500Notes) {
        this.cnt500Notes -= cnt500Notes;
    }
    public void deduct100Notes(int cnt100Notes) {
        this.cnt100Notes -= cnt100Notes;
    }

    public void setATMBalance(int balance, int cnt2kNotes, int cnt500Notes, int cnt100Notes) {
        this.balance = balance;
        this.cnt2kNotes = cnt2kNotes;
        this.cnt500Notes = cnt500Notes;
        this.cnt100Notes = cnt100Notes;
    }

    public void printCurrentATMStatus(){
        System.out.println("Balance: " + balance);
        System.out.println("2kNotes: " + cnt2kNotes);
        System.out.println("500Notes: " + cnt500Notes);
        System.out.println("100Notes: " + cnt100Notes);

    }

}

class ATMState {
    public void insertCard(ATM atm, Card card) {
        System.out.println("This operation is not allowed at this stage");
    }
    public void authenticatePin(ATM atm, Card card, int pin) {
        System.out.println("This operation is not allowed at this stage");
    }
    public void selectOperation(ATM atm, Card card, TransactionType transactionType) {
        System.out.println("This operation is not allowed at this stage");
    }
    public void displayBalance(ATM atm, Card card) {
        System.out.println("This operation is not allowed at this stage");
    }
    public void cashWithdrawal(ATM atm, Card card, int amount) {
        System.out.println("This operation is not allowed at this stage");
    }
    public void returnCard() {
        System.out.println("This operation is not allowed at this stage");
    }
    public void exit(ATM atm) {
        System.out.println("This operation is not allowed at this stage");
    }
}

class IdleState extends ATMState {
    @Override
    public void insertCard(ATM atm, Card card) {
        System.out.println("card is inserted");
        atm.setAtmState(new HashCardState());
    }
}

class HashCardState extends ATMState {
    @Override
    public void authenticatePin(ATM atm, Card card, int pin) {
        card.checkPin(pin);
        atm.setAtmState(new SelectOperationState());
    }

    @Override
    public void returnCard() {
        System.out.println("return done");
    }

    @Override
    public void exit(ATM atm) {
        this.returnCard();
        atm.setAtmState(new IdleState());
        System.out.println("Exit done");
    }
}

class SelectOperationState extends ATMState {
    @Override
    public void exit(ATM atm) {
        this.returnCard();
        atm.setAtmState(new IdleState());
        System.out.println("Exit done");
    }

    @Override
    public void returnCard() {
        System.out.println("return done");
    }

    @Override
    public void selectOperation(ATM atm, Card card, TransactionType transactionType) {
        switch (transactionType) {

            case CASH_WITHDRAWAL:
                atm.setAtmState(new CashWithdrawalState());
                break;
            case BALANCE_CHECK:
                atm.setAtmState(new CheckBalanceState());
                break;
            default: {
                System.out.println("Invalid Option");
                exit(atm);
            }
        }
    }
}

class CashWithdrawalState extends ATMState {
    @Override
    public void cashWithdrawal(ATM atm, Card card, int amount) {
        if(atm.getBalance() < amount) {
            System.out.println("ATM is not having sufficient money");
        } else if (card.getBankAccount().getBalance() < amount) {
            System.out.println("Your account is not having sufficient money");
        } else {
            card.getBankAccount().withdrawBalance(amount);
            WithdrawProcessor withdrawProcessor = new Withdraw2kNotes(new Withdraw500Notes(new Withdraw100Notes(null)));
            withdrawProcessor.withdraw(atm, amount);
        }
    }

    @Override
    public void exit(ATM atm) {
        this.returnCard();
        atm.setAtmState(new IdleState());
        System.out.println("Exit done");
    }

    @Override
    public void returnCard() {
        System.out.println("return done");
    }

}

class CheckBalanceState extends ATMState {
    @Override
    public void displayBalance(ATM atm, Card card) {
        System.out.println("Your Account Balance is: "+card.getBankAccount().getBalance());
    }

    @Override
    public void exit(ATM atm) {
        this.returnCard();
        atm.setAtmState(new IdleState());
        System.out.println("Exit done");
    }

    @Override
    public void returnCard() {
        System.out.println("return done");
    }
}

class WithdrawProcessor {
    WithdrawProcessor nxtProcessor;

    public WithdrawProcessor(WithdrawProcessor nxtProcessor) {
        this.nxtProcessor = nxtProcessor;
    }

    public void withdraw(ATM atm, int amount) {
        if(nxtProcessor != null) nxtProcessor.withdraw(atm, amount);
    }
}

class Withdraw2kNotes extends WithdrawProcessor {
    public Withdraw2kNotes(WithdrawProcessor nxtProcessor) {
        super(nxtProcessor);
    }

    public void withdraw(ATM atm, int amount) {
        int notesCnt = amount/2000;
        if(atm.getCnt2kNotes() < notesCnt) notesCnt = atm.getCnt2kNotes();
        int amountLeft = amount - notesCnt*2000;
        atm.deduct2kNotes(notesCnt);
        atm.deductBalance(notesCnt*2000);
        if(amountLeft == 0) return;
        super.nxtProcessor.withdraw(atm, amountLeft);
    }
}

class Withdraw500Notes extends WithdrawProcessor {
    public Withdraw500Notes(WithdrawProcessor nxtProcessor) {
        super(nxtProcessor);
    }

    public void withdraw(ATM atm, int amount) {
        int notesCnt = amount/500;
        if(atm.getCnt500Notes() < notesCnt) notesCnt = atm.getCnt500Notes();
        int amountLeft = amount - notesCnt*500;
        atm.deduct500Notes(notesCnt);
        atm.deductBalance(notesCnt*500);
        if(amountLeft == 0) return;
        super.nxtProcessor.withdraw(atm, amountLeft);
    }
}

class Withdraw100Notes extends WithdrawProcessor {
    public Withdraw100Notes(WithdrawProcessor nxtProcessor) {
        super(nxtProcessor);
    }

    public void withdraw(ATM atm, int amount) {
        int notesCnt = amount/100;
        if(atm.getCnt100Notes() < notesCnt) notesCnt = atm.getCnt100Notes();
        int amountLeft = amount - notesCnt*100;
        atm.deduct100Notes(notesCnt);
        atm.deductBalance(notesCnt*100);
        if(amountLeft == 0) return;
        super.nxtProcessor.withdraw(atm, amountLeft);
    }
}

class User {
    private Card card;

    public User(Card card) {
        this.card = card;
    }
    public Card getCard() {
        return card;
    }
}

class Card {
    private final int number;
    private final Date expiryDate;
    private final String holderName;
    private final BankAccount bankAccount;

    public Card(BankAccount bankAccount) {
        this.number = 1234;
        this.expiryDate = new Date();
        this.holderName = "John Doe";
        this.bankAccount = bankAccount;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public boolean checkPin(int pin) {
        return true;
    }
}

class BankAccount {
    private int balance;

    public BankAccount(int balance) {
        this.balance = balance;
    }

    public int getBalance() {
        return balance;
    }

    public void withdrawBalance(int amount) {
        balance -= amount;
    }
}

enum TransactionType {

    CASH_WITHDRAWAL,
    BALANCE_CHECK;

    public static void showAllTransactionTypes(){

        for(TransactionType type: TransactionType.values()){
            System.out.println(type.name());
        }
    }
}

