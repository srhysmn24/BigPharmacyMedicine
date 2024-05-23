import java.util.*;
import java.io.*;
import java.text.*;

public class MedicineQueue {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Queue medicineQueue = new Queue();
        double totalPrices = calculateTotalPrices(medicineQueue);
        try {
            String inputFilePath = "D:\\UiTM\\SEM 3\\CSC248\\PROJECT\\MEDICINE\\InputMedicine.txt"; //Change the path based on InputMedicine.txt location 
            String outputFilePath = "D:\\UiTM\\SEM 3\\CSC248\\PROJECT\\MEDICINE\\UpdatedMedicine.txt";
            readFile(inputFilePath, medicineQueue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        //MAIN MENU 
        int choice;
        System.out.println("===============================================================================");
        System.out.println("| ｡･:*:･ﾟ★,｡･:*:･ﾟ☆          WELCOME TO BIG PHARMACY         ｡･:*:･ﾟ★,｡･:*:･ﾟ☆ |");
        System.out.println("===============================================================================");
        do {
            System.out.println("\n\n========================================================================"); 
            System.out.println("|                 MAIN MENU                                            |");
            System.out.println("|======================================================================|");
            System.out.println("|  [1]  |  Display all customers' records                              |");
            System.out.println("|  [2]  |  Search and update the membership status                     |");
            System.out.println("|  [3]  |  Calculate total prices for all the medicine purchased       |");
            System.out.println("|  [4]  |  Count medicine type that customers have purchased           |");
            System.out.println("|  [5]  |  Calculate average total prices of medicine purchased        |");
            System.out.println("|  [6]  |  Remove customers' record that purchased less than 2 items   |");
            System.out.println("|  [7]  |  Exit the program                                            |");
            System.out.println("========================================================================");
            System.out.print("\nEnter your choice (1-7): ");
            choice = in.nextInt();

            switch (choice) {
                case 1:
                    displayRecord(medicineQueue);
                    break;
                case 2:
                    updateMemberStatusMenu(medicineQueue, in);
                    break;
                case 3:
                    totalPrices = calculateTotalPrices(medicineQueue);
                    System.out.println("Total Prices of Medicines: RM " + totalPrices);
                    break;
                case 4:
                    countMedicineTypes(medicineQueue);
                    break;
                case 5:
                    calculateAverage(medicineQueue);
                    break;
                case 6:
                    removeLessThanTwoItems(medicineQueue);
                    break;
                case 7:
                    System.out.println("Exiting program. Thank you!");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 until 7 only.");
            }
        } while (choice != 7);
    }
    
    public static void readFile(String filePath, Queue medicineQueue) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String dataRow;
        while ((dataRow = br.readLine()) != null) {
            String[] data = dataRow.split(";");
            Medicine med = new Medicine(data[0], data[1], data[2], Integer.parseInt(data[3]), data[4], data[5], data[6], Integer.parseInt(data[7]), Double.parseDouble(data[8]), data[9].charAt(0));
            medicineQueue.enqueue(med);
        }
        br.close();
    }
    
    public static void displayRecord(Queue medicineQueue) {
        Queue tempQueue = new Queue();
        while (!medicineQueue.isEmpty()) {
            Medicine med = (Medicine) medicineQueue.dequeue();
            System.out.println(med);
            tempQueue.enqueue(med); // Storing in temporary queue
        }

        while (!tempQueue.isEmpty()) {
            medicineQueue.enqueue(tempQueue.dequeue());
        }
    }
    
    public static void updateMemberStatusMenu(Queue medicineQueue, Scanner in) {
        System.out.print("Enter customer's name to update the membership status: ");
        String custName = in.next();
        System.out.print("Enter new status membership [yes/no]: ");
        String newStatus = in.next();
        updateMemberStatus(medicineQueue, custName, newStatus);
    }
        public static void updateMemberStatus(Queue medicineQueue, String custName, String newStatus) {
        Queue tempQueue = new Queue();
        boolean found = false;
        while (!medicineQueue.isEmpty()) {
            Medicine med = (Medicine) medicineQueue.dequeue();
            if (med.getCustName().equalsIgnoreCase(custName)) {
                med.setMemberStatus(newStatus);
                found = true;
            }
            tempQueue.enqueue(med);
        }
    
        while (!tempQueue.isEmpty()) {
            medicineQueue.enqueue(tempQueue.dequeue());
        }
        if (found) {
            System.out.println("Membership Status Updated for customer name " + custName + " to " + newStatus);
            System.out.println("Customers' records has been updated in UpdatedMedicine.txt file");
            updateFile("D:\\UiTM\\SEM 3\\CSC248\\PROJECT\\MEDICINE\\InputMedicine.txt",
                        "D:\\UiTM\\SEM 3\\CSC248\\PROJECT\\MEDICINE\\UpdatedMedicine.txt" ,
                       medicineQueue);
        } else {
            System.out.println("Customer not found in the records.");
        }
    }
    
    public static void updateFile(String inputFilePath, String outputFilePath, Queue medicineQueue) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));
            Queue tempQueue = new Queue();
    
            while (!medicineQueue.isEmpty()) {
                Medicine med = (Medicine) medicineQueue.dequeue();
                writer.write(med.toString());
                writer.newLine();
                tempQueue.enqueue(med);
            }
    
            while (!tempQueue.isEmpty()) {
                medicineQueue.enqueue(tempQueue.dequeue());
            }
    
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static double calculateTotalPrices(Queue medicineQueue) {
        double totalPrices = 0.0;
        Queue tempQueue = new Queue();
        while (!medicineQueue.isEmpty()) {
            Medicine med = (Medicine) medicineQueue.dequeue();
            totalPrices += med.calculateTotalPrice();
            tempQueue.enqueue(med);
        }
        while (!tempQueue.isEmpty()) {
            medicineQueue.enqueue(tempQueue.dequeue());
        }
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(totalPrices));
    }
    
    public static void countMedicineTypes(Queue medicineQueue) {
        int countS = 0; 
        int countM = 0;
        Queue tempQueue = new Queue(); //Temporary queue
        while (!medicineQueue.isEmpty()) {
            Medicine med = (Medicine) medicineQueue.dequeue();
            if (med.getMedType() == 's') {
                countS++;   //count the number of Medicine Type - Soluble
            }
            else if (med.getMedType() == 'm') {
                countM++;   //count the number of Medicine Type - Medical Supplies
            }
            tempQueue.enqueue(med);
        }

        while (!tempQueue.isEmpty()) {
            medicineQueue.enqueue(tempQueue.dequeue());
        }
        System.out.println("The number of medicine type [Soluble] :" + countS);
        System.out.println("The number of medicine type [Medical Supplies] :" + countM);
    }
    
    public static void calculateAverage(Queue medicineQueue) {
        double totalPrices = 0.0;
        double averageTotalPrices = 0.0;
        int count = 0;
        Queue tempQueue = new Queue(); // Create a temporary queue

        while (!medicineQueue.isEmpty()) {
            Medicine medicine = (Medicine) medicineQueue.dequeue();
            totalPrices += medicine.calculateTotalPrice();
            tempQueue.enqueue(medicine); // Enqueue the medicine back to temporary queue
            count++;
        }

        while (!tempQueue.isEmpty()) {
            medicineQueue.enqueue(tempQueue.dequeue());
        }
        DecimalFormat df = new DecimalFormat("#.##");
        averageTotalPrices = Double.parseDouble(df.format(totalPrices / count));
        System.out.println("The average total prices of medicine purchased by the customers: RM  " + averageTotalPrices);
    }
    
    public static void removeLessThanTwoItems(Queue medicineQueue) {
        Queue tempQueue = new Queue();
        while (!medicineQueue.isEmpty()) {
            Medicine med = (Medicine) medicineQueue.dequeue();
            if (med.getMedQty() >= 2) {
                tempQueue.enqueue(med); //Enqueue to tempQueue as we still want the record
            }
        }

        while (!tempQueue.isEmpty()) {
            medicineQueue.enqueue(tempQueue.dequeue());
        }
        System.out.println("\nRemaining Medicine Records after removal:");
        System.out.println(("\n" + medicineQueue.toString()));
    }
}