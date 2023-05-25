package com.monitoringService.monitoringService;

import com.montioringService.utilities.Reader;

public class LogicEngine {
	static int multiplicationFactor = 7;

	public static String fetchTotalCountFor(String flow, String object, String crmName, String tenantSize,
			String tenantId, String offset, String growthStage , String previousOffset) {
		// fetch the bulk sync count for accounts
		int limit = Integer.parseInt(Reader.getData("tenantSize_" + tenantSize, "salesforce_bulk sync_accounts"));
		if (object.equalsIgnoreCase("accounts") && flow.equalsIgnoreCase("bulk sync")) {
			return Integer.toString(limit); // return the count for bulk sync for accounts
		} else { 
			// same function for incremental and bulk for contact, lead and opportunity
			int i = Integer.parseInt(offset);

			int totalCount = 0;
			if (flow.equalsIgnoreCase("incremental sync"))
			{
				//swapping limit with offset
				
				limit = i;
				i = Integer.parseInt(previousOffset);
			}
			if (object.equalsIgnoreCase("contacts")) {
				while (i < limit) {
					totalCount = totalCount + fetchTheNumberOfContacts(i);
					i++;
				}
				return Integer.toString(totalCount);
			}
			else if (object.equalsIgnoreCase("opportunity"))
			{
				while (i < limit) {
					totalCount = totalCount + fetchTheNumberOfDeals(i);
					i++;
				}
				return Integer.toString(totalCount);
			}
			else if (object.equalsIgnoreCase("leads"))
			{
				while (i < limit) {
					totalCount = totalCount + fetchTheNumberOfDeals(i);
					i++;
				}
				return Integer.toString(totalCount);
			}
			else //for accounts incremental flow
			{
				totalCount = (int)((limit * Float.parseFloat(Reader.getData("common", "Growth_Stage_"+ growthStage.toLowerCase())))/100);
				return Integer.toString(totalCount);
			}
		}
	}

	public static void main(String[] args) {
		int totalOpportunityCreated = 0;
		int twoDigitNumber = 0;
		int singleDigit = 0;
		int zeroCount = 0;
		int i = 0;
		int limit = 40000;
		while (i < limit) {
			int numberOfDeals = fetchTheNumberOfDeals(i);
			totalOpportunityCreated = totalOpportunityCreated + numberOfDeals;
			if (numberOfDeals >= 10 && numberOfDeals < 100) {
				System.out.println(numberOfDeals);
				twoDigitNumber = twoDigitNumber + 1;
			} else if (numberOfDeals < 10 && numberOfDeals > 0) {
				singleDigit = singleDigit + 1;
			} else if (numberOfDeals == 0) {
				zeroCount = zeroCount + 1;
			} else
				System.out.println("out of context " + numberOfDeals);
			i = i + 1;
		}
		System.out.println("Total accounts: " + limit);
		System.out.println("accounts with more than 10 and less than 100 deals: " + twoDigitNumber);
		System.out.println("accounts with more than 1 and less than 10 deals: " + singleDigit);
		System.out.println("accounts with no deals: " + zeroCount);
		System.out.println("Total Opportunities Created: " + totalOpportunityCreated);

		int fourDigitNumber = 0;
		int threeDigitNumber = 0;
		twoDigitNumber = 0;
		singleDigit = 0;
		zeroCount = 0;
		int totalContactCreated = 0;
		i = 0;
		System.out.println();
		while (i < limit) {
			int numberOfDeals = fetchTheNumberOfContacts(i);
			totalContactCreated = totalContactCreated + numberOfDeals;

			if (numberOfDeals >= 1000 && numberOfDeals < 10000) {
				// System.out.println("four digit :" + numberOfDeals);
				fourDigitNumber = fourDigitNumber + 1;
			} else if (numberOfDeals >= 100 && numberOfDeals < 1000) {
				// System.out.println("three digit :" + numberOfDeals);
				threeDigitNumber = threeDigitNumber + 1;
			} else if (numberOfDeals >= 10 && numberOfDeals < 100) {
				twoDigitNumber = twoDigitNumber + 1;
			} else if (numberOfDeals < 10 && numberOfDeals > 0) {
				singleDigit = singleDigit + 1;
			} else if (numberOfDeals == 0) {
				// System.out.println("zero for : " + i);
				zeroCount = zeroCount + 1;
			} else
				System.out.println("out of context " + numberOfDeals);
			i = i + 1;
		}
		System.out.println("Total accounts: " + limit);
		System.out.println("accounts with more than 1000 contacts: " + fourDigitNumber);
		System.out.println("accounts with more than 100 and less than 1000 contacts: " + threeDigitNumber);
		System.out.println("accounts with more than 10 and less than 100 contacts: " + twoDigitNumber);
		System.out.println("accounts with more than 1 and less than 10 contacts: " + singleDigit);
		System.out.println("accounts with no contact: " + zeroCount);
		System.out.println("Total contacts Created: " + totalContactCreated);
		System.out.println(generateID(2053));
	}

	public static String generateID(int number) {
		String id = String.format("A%016d", number);
		return id;
	}

	public static int calculateDigitSum(int number) {
		int sum = 0;
		int remainingDigits = Math.abs(number); // Handle negative numbers

		while (remainingDigits > 0) {
			int digit = remainingDigits % 10; // Get the rightmost digit
			sum += digit;
			remainingDigits /= 10; // Remove the rightmost digit
		}

		return sum;
	}

	public static int fetchTheNumberOfDeals(int accountId) {
		int percentage = (accountId % 100) * multiplicationFactor;
		if (percentage >= 100)
			percentage = percentage % 100;
		if (isPrime(percentage) && calculateDigitSum(accountId) > 2 && calculateDigitSum(accountId) <= 5) {
			return percentage;
		} else if (isPrime(percentage) && calculateDigitSum(accountId) > 5 && calculateDigitSum(accountId) < 13) {
			return (percentage % 10);
		} else {
			return 0;
		}
	}

	public static int fetchTheNumberOfContacts(int accountId) {
		int percentage = (accountId % 100) * multiplicationFactor;
		if (accountId >= 10000) {
			if (isPrime(accountId) && calculateDigitSum(accountId) == 4) {
				return accountId % 10000;
			}
		}
		if (accountId >= 1000) {
			if (isPrime(accountId) && calculateDigitSum(accountId) == 3) {
				return accountId % 1000;
			}
		}
		if (percentage >= 100)
			percentage = percentage % 100;
		if (isPrime(percentage) && calculateDigitSum(accountId) > 2 && calculateDigitSum(accountId) <= 10) {
			return percentage;
		} else if (calculateDigitSum(accountId) > 0 && calculateDigitSum(accountId) < 200) {
			return (percentage % 10);
		} else {
			return 0;
		}
	}

	private static boolean isPalindrome(int number) {
		String numString = String.valueOf(number);
		int left = 0;
		int right = numString.length() - 1;

		while (left < right) {
			if (numString.charAt(left) != numString.charAt(right)) {
				return false;
			}
			left++;
			right--;
		}

		return true;
	}

	public static boolean isPalindromicPrime(int number) {
		if (number < 2) {
			return false;
		}

		if (!isPrime(number)) {
			return false;
		}

		return isPalindrome(number);
	}

	public static boolean isMersennePrime(int exponent) {
		if (exponent < 2) {
			return false;
		}

		int number = (int) Math.pow(2, exponent) - 1;
		return isPrime(number);
	}

	public static boolean isSophieGermainPrime(int number) {
		if (number <= 1) {
			return false;
		}

		if (!isPrime(number)) {
			return false;
		}

		int sophieGermainNumber = 2 * number + 1;
		return isPrime(sophieGermainNumber);
	}

	public static boolean isPrime(int number) {
		if (number <= 1) {
			return false;
		}

		for (int i = 2; i <= Math.sqrt(number); i++) {
			if (number % i == 0) {
				return false;
			}
		}

		return true;
	}

	public static boolean isLeapYear(int year) {
		// A year is a leap year if it is divisible by 4
		// and not divisible by 100, except if it is divisible by 400

		if (year % 4 == 0) {
			if (year % 100 == 0) {
				if (year % 400 == 0) {
					return true; // Divisible by 400, so a leap year
				} else {
					return false; // Divisible by 100, not a leap year
				}
			} else {
				return true; // Divisible by 4, but not by 100, so a leap year
			}
		} else {
			return false; // Not divisible by 4, not a leap year
		}
	}

}
