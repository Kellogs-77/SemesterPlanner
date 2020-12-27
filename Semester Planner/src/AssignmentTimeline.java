import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

public class AssignmentTimeline {
	
	private static String[] maxHeap;
	private static String[] orderedSemWork;
	private static ArrayList<String> totalSemesterWork;
	private static String currYear;
	private static String currDate;
	

	public static ArrayList<String> minHeapToUser(Scanner sc) { //goes from minheap to user's schedule
		assignmentPlotter(sc);
		
		ArrayList<String> result = new ArrayList<String>();
		boolean[] added = new boolean[maxHeap.length];
		
		for(String s: maxHeap) {
			 
			findAndAdd(added, totalSemesterWork, s, result);
			
		}
		
		for(int i = 0; i < result.size(); i++) {	
			nullRemover(result.get(i), i, result);
		}
		
		
		return result;
	}
	public static void assignmentPlotter(Scanner sc) {
		totalSemesterWork = new ArrayList<>();
		String workPlusDate;
		while(sc.hasNext()) {
			workPlusDate = sc.nextLine();
			workPlusDate+=currYear;
			totalSemesterWork.add(workPlusDate);

		}
		
		
		maxHeap = new String[totalSemesterWork.size()];
		
		
		
		for(int i = 0; i < maxHeap.length; i++) {
			String t = totalSemesterWork.get(i).substring((returnStringIndex(totalSemesterWork.get(i), ',')+1));	
			t = t.substring(1, returnStringIndex(t, 'n'));
			int ind = returnStringIndex(t, '/');
			if(ind == 1) {
				t = "0"+t;
			}
			if(t.length() == 4) {
				t = t.substring(0, 3)+"0"+t.substring(3);
			}

			maxHeap[i] = t;
		}
		
		
		sort();
		
	}
	public static void sort() {
		
		//build heap
		for(int k = (maxHeap.length-2)/2; k >=0; k--) {
			siftDown(k, maxHeap.length);
		}
		
		
		
		//sort heap
		for(int n = maxHeap.length; n > 1; n --) {
			String max = maxHeap[0];
			maxHeap[0] = maxHeap[n-1];
			maxHeap[n-1] = max;
			
			siftDown(0, n-1);
			
			
		}
		
		
	}
	
	public static void siftDown(int k, int n) {
		while (2*k+1 < n) {  // not a leaf, there is at least a left child
			int maxIndex = 2*k+1;   // set maxIndex to left child index
			int rightChild = maxIndex + 1;
			int kInd = Integer.parseInt(maxHeap[k].substring(0, 2));
			int maxInd = Integer.parseInt(maxHeap[maxIndex].substring(0, 2));
			if (rightChild < n) { // there is a right child
				int rC = Integer.parseInt(maxHeap[rightChild].substring(0, 2));
				if(rC > maxInd) {maxIndex = rightChild; }
			}

			

			

			if(maxInd > kInd) {
				String temp = maxHeap[maxIndex];
				maxHeap[maxIndex] = maxHeap[k];
				maxHeap[k] = temp;
				k = maxIndex;  // for next iteration
			} 
			else if(maxInd == kInd){ // if months are same, check days
				
				int maxIndDay = Integer.parseInt(maxHeap[maxIndex].substring(3));
				int kIndDay = Integer.parseInt(maxHeap[k].substring(3));

				if(maxIndDay > kIndDay) {
					String temp = maxHeap[maxIndex];
					maxHeap[maxIndex] = maxHeap[k];
					maxHeap[k] = temp;
					k = maxIndex; 
				}
				else {
					break;
				}
				
			} 
			else {
				break;
			}
			
		}

	}
	
	private static void findAndAdd(boolean[] b, ArrayList<String> list, String s, ArrayList<String> result) { //find and add the corresponding assignment plus date in the sorted order
		if(s.charAt(0) == '0') {
			s = s.substring(1);
		}
		if(s.charAt(2) == '0') {
			s = s.substring(0, 2) + s.substring(3);
		} else if(s.charAt(3) == '0') {
			if(s.charAt(2)== '/') {
				s = s.substring(0, 3) + s.substring(4);
			}
		}
		for(int i = 0; i < list.size(); i++) {
			String cool = (list.get(i).substring(returnStringIndex(list.get(i), ',')+2, (list.get(i).length()-4)));
			int c = cool.compareTo(s);
			if(c == 0) {
				if(!b[i]) {
					b[i] = true;
					result.add(list.get(i));
					break;
				}
			}
		}
	}
	private static int returnStringIndex(String string, char character) {
		int index = -1;
		char c;
		for(int i = 0; i < string.length(); i++) {
			c = string.charAt(i);
			if(c == character) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	private static void nullRemover(String s, int j, ArrayList<String> result) {
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if(c == 'n') {
				if(s.charAt(i+1) == 'u' && s.charAt(i+2) == 'l' && s.charAt(i+3) == 'l') {
					s = s.substring(0, i);
					result.set(j, s);
					break;
				}
			}
		}
	}
	
	private static void daysInBetween(ArrayList<String> sortedList) { //feature that will allow to see how many days left for each assignment
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDate date1 = LocalDate.parse(currDate, formatter);
		for(int i = 0; i < sortedList.size(); i++) {
			String listDate = sortedList.get(i).substring(returnStringIndex(sortedList.get(i), ',')+2) + "/" + currDate.substring(currDate.length()-4, currDate.length());
			int ind = returnStringIndex(listDate, '/');
			if(ind == 1) {
				listDate = "0"+listDate;
			}
			if(listDate.length() == 9) {
				listDate = listDate.substring(0, 3)+"0"+listDate.substring(3);
			}
			LocalDate date2 = LocalDate.parse(listDate, formatter);
			long daysInBetween = ChronoUnit.DAYS.between(date1, date2);
			String s;
			if(daysInBetween < 0 && daysInBetween!=-1) {
				 s = sortedList.get(i) + " - Woah, looks like this was due..." +daysInBetween*-1+" days ago!";
			} else if(daysInBetween == -1) {
				s = sortedList.get(i) + " - Woah, looks like this was due...one day ago!";
			} else if(daysInBetween == 1) {
				s = sortedList.get(i) + " - You got 1 day left it's grind time!";
			} else {
				 s = sortedList.get(i) + " - " + daysInBetween + " days left!";
			}
			
			sortedList.set(i,  s);
		}
	}

	
	public static void main(String[] args) throws FileNotFoundException {
		
		// TODO Auto-generated method stub
		
		LocalDate today = LocalDate.now();
	
		currDate = today.toString();
		
		String temp = currDate;
		
		currDate = temp.substring(5, 7)+"/"+temp.substring(8)+"/"+temp.substring(0, 4);

			    
		Scanner data = new Scanner(System.in);  // Create a Scanner object
	    System.out.println("Enter file name");
	    String fileName = data.nextLine();  // Read user input
		
		Scanner sc = new Scanner(new File(fileName));
				
		ArrayList<String> finalList = minHeapToUser(sc);
		
		
		if(finalList.size() == 0) {
			System.out.println("No input");
		} else {
			if(finalList.size() < 10) {
				System.out.println("If all your assignments don't suck, and your semester isn't ending soon, it doesn't look too bad: ");
			} else if(finalList.size() > 10 && finalList.size() < 40) {
				System.out.println("Well, it doesn't look easy but it could be worse(unless of course your semester ends tomorrow. if it does yikes) : ");
			} else {
				System.out.println("Yeah...you got to grind. Here's your assignments: ");
			}
		}
		
		daysInBetween(finalList);
		
		
		
		
		for(String s: finalList) {
			System.out.println(s);
		}
		
		
		
		
		

	}

}
