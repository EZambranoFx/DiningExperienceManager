package main;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@SuppressWarnings("PMD.UseUtilityClass")
public class DiningExperienceManager {
	private static final double BASE_COST = 5.0;
	private static final double DISCOUNT_5_MEALS = 0.10;
	private static final double DISCOUNT_10_MEALS = 0.20;
	private static final double DISCOUNT_OVER_50 = 10.0;
	private static final double DISCOUNT_OVER_100 = 25.0;

	public static void main(String[] args) {
		Map<String, Double> menu = createMenu();
		Map<String, Integer> order = new HashMap<>();
		double totalCost = BASE_COST;

		final Scanner scanner = new Scanner(System.in);

		System.out.println("Welcome to Dining Experience Manager!");
		displayMenu(menu);

		while (true) {
			System.out.print("Enter a meal from the menu (or 'done' to confirm): ");
			String meal = scanner.nextLine().trim().toLowerCase();

			if ("done".equals(meal)) {
				break;
			}

			if (!menu.containsKey(meal)) {
				System.out.println("Invalid meal selection. Please select a meal from the menu.");
				continue;
			}

			System.out.print("Enter the quantity for " + meal + ": ");
			int quantity = getValidQuantity(scanner);

			if (quantity > 0 && quantity <= 100) {
				order.put(meal, quantity);
				totalCost += menu.get(meal) * quantity;
			} else {
				System.out.println("Invalid quantity. Please enter a positive integer between 1 and 100.");
			}
		}

		displayOrder(order);
		final double discountedCost = applyDiscounts(order, totalCost);

		if (discountedCost >= 0) {
			System.out.println("Total cost: $" + (int) discountedCost);
		} else {
			System.out.println("Order canceled or invalid input. Returning -1.");
		}
	}

	private static Map<String, Double> createMenu() {
		Map<String, Double> menu = new HashMap<>();
		menu.put("meal1", 10.0);
		menu.put("meal2", 15.0);
		menu.put("meal3", 20.0);
		// Add more meals and prices as needed
		return menu;
	}

	private static void displayMenu(Map<String, Double> menu) {
		System.out.println("Menu:");
		for (Map.Entry<String, Double> entry : menu.entrySet()) {
			System.out.println(entry.getKey() + ": $" + entry.getValue());
		}
	}

	private static int getValidQuantity(final Scanner scanner) {
		int quantity;
		while (true) {
			try {
				quantity = Integer.parseInt(scanner.nextLine());
				if (quantity > 0) {
					return quantity;
				} else {
					System.out.println("Please enter a positive integer greater than zero.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a positive integer greater than zero.");
			}
		}
	}

	private static void displayOrder(Map<String, Integer> order) {
		System.out.println("Selected Meals:");
		for (Map.Entry<String, Integer> entry : order.entrySet()) {
			System.out.println(entry.getKey() + " x" + entry.getValue());
		}
	}

	private static double applyDiscounts(Map<String, Integer> order, double totalCost) {
		int totalQuantity = order.values().stream().mapToInt(Integer::intValue).sum();
		double discountedCost = totalCost;

		if (totalQuantity > 5) {
			discountedCost *= 1 - DISCOUNT_5_MEALS;
		}

		if (totalQuantity > 10) {
			discountedCost *= 1 - DISCOUNT_10_MEALS;
		}

		if (discountedCost > 100) {
			discountedCost -= DISCOUNT_OVER_100;
		} else if (discountedCost > 50) {
			discountedCost -= DISCOUNT_OVER_50;
		}

		return discountedCost;
	}
}