import java.util.*;
import java.io.*;
import java.text.*;

public class MedicineLinkedList{
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        LinkedList medLL = new LinkedList();

        try {
            String inputFilePath = "D:\\UiTM\\SEM 3\\CSC248\\PROJECT\\MEDICINE\\InputMedicine.txt"; //Change the path based on InputMedicine.txt location
            String outputFilePath = "D:\\UiTM\\SEM 3\\CSC248\\PROJECT\\MEDICINE\\UpdatedMedicine.txt"; 
            readFile(inputFilePath, medLL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // MAIN MENU
        int choice;
        System.out.println("===============================================================================");
        System.out.println("|                           WELCOME TO BIG PHARMACY                           |");
        System.out.println("===============================================================================");
        do {
            System.out.println("\n\n========================================================================");
            System.out.println("|                              MAIN MENU                               |");
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
                    displayRecord(medLL);
                    break;
                case 2:
                    updateMemberStatusMenu(medLL, in);
                    break;
                case 3:
                    double totalPrices = calculateTotalPrices(medLL);
                    System.out.println("Total Prices of Medicines: RM " + totalPrices);
                    break;
                case 4:
                    countMedicineTypes(medLL);
                    break;
                case 5:
                    calculateAverage(medLL);
                    break;
                case 6:
                    removeLessThanTwoItems(medLL);
                    break;
                case 7:
                    System.out.println("Exiting program. Thank you!");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 until 7 only.");
            }
        } while (choice != 7);

        in.close(); // Close the Scanner to prevent resource leak
    }

    public static void readFile(String filePath, LinkedList medLL) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String dataRow;
        while ((dataRow = br.readLine()) != null) {
            String[] data = dataRow.split(";");
            Medicine med = new Medicine(data[0], data[1], data[2], Integer.parseInt(data[3]), data[4], data[5], data[6], Integer.parseInt(data[7]), Double.parseDouble(data[8]), data[9].charAt(0));
            medLL.addLast(med);
        }
        br.close();
    }

     public static LinkedList displayRecord(LinkedList medLL) {
        System.out.println("\nDetails of all the customer: ");
        Object medicineObject = medLL.getFirst();

        while (medicineObject != null) {
            Medicine medicine = (Medicine) medicineObject;
            System.out.println(medicine.toString());
            medicineObject = medLL.getNext();
        }

        return medLL;
    }
    
    public static void updateFile(String inputFilePath, String outputFilePath, LinkedList medLL) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));
            LinkedList tempLL = new LinkedList();

            while (!medLL.isEmpty()) {
                Medicine med = (Medicine) medLL.removeFirst();
                writer.write(med.toString());
                writer.newLine();
                tempLL.addLast(med);
            }

            while (!tempLL.isEmpty()) {
                medLL.addLast(tempLL.removeFirst());
            }
    
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

       public static void updateMemberStatus(LinkedList medLL, String custName, String newStatus) {
        LinkedList tempLL = new LinkedList();
        boolean found = false;
        while (!medLL.isEmpty()) {
            Medicine medicine = (Medicine) medLL.removeFirst();
            if (medicine.getCustName().equalsIgnoreCase(custName)) {
                medicine.setMemberStatus(newStatus);
                found = true;
            }
            tempLL.addLast(medicine);
        }
    
        while (!tempLL.isEmpty()) {
            medLL.addLast(tempLL.removeFirst());
        }
        if (found) {
            System.out.println("Membership Status Updated for customer name " + custName + " to " + newStatus);
            System.out.println("Customer's records has been updated in UpdatedMedicine.txt file");
            updateFile("D:\\UiTM\\SEM 3\\CSC248\\PROJECT\\MEDICINE\\InputMedicine.txt",
                        "D:\\UiTM\\SEM 3\\CSC248\\PROJECT\\MEDICINE\\UpdatedMedicine.txt",medLL);
        } else {
            System.out.println("Customer not found in the records.");
        }
    }
    
    public static void updateMemberStatusMenu(LinkedList medLL, Scanner in) {
        System.out.print("Enter customer's name to update the membership status: ");
        String custName = in.next();
        System.out.print("Enter new status membership [yes/no]: ");
        String newStatus = in.next();
        updateMemberStatus(medLL, custName, newStatus);
    }
    
    public static double calculateTotalPrices(LinkedList medLL) {
        // Process 3: Calculate total prices for all the medicine purchased by the customers
        Medicine medicine = (Medicine) medLL.getFirst();
        double totalPrices = 0.0;

        while (medicine != null) {
            totalPrices = totalPrices + medicine.calculateTotalPrice();
            medicine = (Medicine) medLL.getNext();
        } 
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(totalPrices));
    }

    public static void countMedicineTypes(LinkedList medLL) {
        int countS = 0;
        int countM = 0;
        Medicine medicine = (Medicine) medLL.getFirst();
        while (medicine != null) {
            if (medicine.getMedType() == 's') {
                countS++;
            } else {
                countM++;
            }

            medicine = (Medicine) medLL.getNext();
        }

        System.out.println("Numbers of customers that purchased medicine type [Soluble]: " + countS);
        System.out.println("Numbers of customers that purchased medicine type [Medical Supplies]: " + countM);
    }
    
    
    public static void calculateAverage(LinkedList medLL) {
        double averageTotalPrices = 0.0;
        double totalPrices = 0.0;
        int count = 0;
        Medicine medicine = (Medicine) medLL.getFirst();

        while (medicine != null) {
            totalPrices += medicine.calculateTotalPrice();
            count++;
            medicine = (Medicine) medLL.getNext();
        }
        DecimalFormat df = new DecimalFormat("#.##");
        averageTotalPrices = Double.parseDouble(df.format(totalPrices / count));
        System.out.println("The average total prices of medicine purchased by the customers: RM " + averageTotalPrices);
    }
    
    public static void removeLessThanTwoItems(LinkedList medLL) {
        LinkedList medTemp = new LinkedList();
        LinkedList medRemove = new LinkedList();
        while (!medLL.isEmpty()) {
            Medicine medicine = (Medicine) medLL.getFirst();
            if (medicine.getMedQty() >= 2) {
                medTemp.addLast(medicine);
            } else {
                medRemove.addLast(medicine);
            }
            medLL.removeFirst();
        }

        while (!medTemp.isEmpty()){
            medLL.addLast(medTemp.removeFirst());
        }
        
        System.out.println("\nRemaining Medicine Records after removal:\n" + medLL);
    }
}