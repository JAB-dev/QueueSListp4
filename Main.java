import java.util.Scanner;
/*
 * Total Marks Main Class: 25
 * Compilation & Correct Execution Marks: 10
 */
public class Main {
	static LinkedQueue<Transaction> buyQueue = new LinkedQueue<Transaction>();
	static LinkedQueue<Transaction> sellQueue = new LinkedQueue<Transaction>();
	static int totalGain = 0;
	
	/**
	 * Process queue of transactions - determine if each transaction is a buy or sell
	 * transaction add it to the appropriate queue
	 * @param transactions - a queue of buy and sell transactions
	 * 15 marks
	 */
	public static void processTransactions(LinkedQueue<String> transactions) {
		while(!transactions.isEmpty()){
			String[] transaction = transactions.dequeue().split(" ");
			try{
				if (transaction[0].equals("BUY")){
					buyQueue.enqueue(new Transaction( Integer.parseInt(transaction[1]), Integer.parseInt(transaction[2])));
					System.out.println("Transaction: "+"BUY "+transaction[1]+" "+transaction[2]);
				}
				else if (transaction[0].equals("SELL")){
					sellQueue.enqueue(new Transaction(Integer.parseInt(transaction[1]), Integer.parseInt(transaction[2])));
					System.out.println("Transaction: "+"SELL "+transaction[1]+" "+transaction[2]);
				}else{
					System.out.println("There was an invalid transaction");
				}
			}catch(NumberFormatException nex){
				System.out.println("There was an invalid transaction");
			}

		}
	}
	
	/**
	 * Calculate capital gain(loss)
	 * Loops through each sell until sells are depeleted and we return the capital gain/loss
	 * As given this means losses are only realised once the stocks are sold
	 * @return totalGain
	 * 10 marks
	 */
	public static Integer calculateCapitalGainLoss() {
		//Loop through each sell until the number of sold shares are met by the number of bought shares
		Integer runningTotal=0;
		while(!sellQueue.isEmpty()){
			Transaction sell = sellQueue.dequeue();
			Integer sellQuantity = sell.getQuantity();
			Integer sellPrice = sell.getUnitPrice();
			while(sellQuantity>0){
				Transaction bought = buyQueue.dequeue();
				Integer buyQuantity = bought.getQuantity();
				Integer buyPrice = bought.getUnitPrice();
				//If the number of shares sold is less than the number of shares bought, subtract the number of shares sold from the number of shares bought
				if(sellQuantity<buyQuantity){
					//System.out.println("Sell quantity is less than buy quantity bought: "+buyQuantity+"Sold: "+sellQuantity);
					buyQuantity-=sellQuantity;
					runningTotal+=(sellPrice-buyPrice)*sellQuantity;
					//because we had less stocks sold than bought we out of buys for this sell
					sellQuantity=0;
					buyQueue.enqueue(new Transaction(buyQuantity,buyPrice));
				}else if(sellQuantity==buyQuantity){
					runningTotal+=(sellPrice-buyPrice)*sellQuantity;
					sellQuantity=0;
				}
				//If the number of shares sold is greater than the number of shares bought, subtract the number of shares bought from the number of shares sold
				else{
					//System.out.println("Sell quantity is greater than buy quantity bought: "+buyQuantity+"Sold: "+sellQuantity+"");
					sellQuantity-=buyQuantity;
					runningTotal+=(sellPrice-buyPrice)*buyQuantity;
				}
			}
		}
		totalGain+=runningTotal;
		return totalGain;

	}
	
	public static void main(String[] args) {
		String response = "";
		Scanner s = new Scanner(System.in);
		LinkedQueue<String> instructionQueue = new LinkedQueue<String>();
		Integer capGainLoss;

		while (!response.toLowerCase().equals("quit")){
			System.out.println("Select option: ");
			System.out.println("1) Enter new transaction in the format BUY <QUANTITY> <PRICE> or SELL <QUANTITY> <PRICE>");
			System.out.println("2) Calculate capital gain or loss");
			System.out.println("or \"quit\" to quit.");
			response = s.nextLine();
			
			switch(response.toLowerCase()){
				case "1": {
					System.out.println("Enter transaction:");
					response = s.nextLine();
					if (!response.equals(""))
						instructionQueue.enqueue(response);
				}
					break;
				case "2": {
					processTransactions(instructionQueue);
					capGainLoss = calculateCapitalGainLoss();
					if (capGainLoss == null)
						System.out.println("Unmatched sell transaction(s).");
					else
						System.out.println("Capital Gain/Loss: "+capGainLoss);
				}
					break;
				case "quit": break;
				default: System.out.println("Incorrect option selected. Please try again.");
			}			
		}
	}
}