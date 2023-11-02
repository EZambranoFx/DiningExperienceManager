package main;

import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * The DiningExperienceManager class 
 *
 * @author Enrique Zambrano
 * @version 1.0
 * @since 2023-11-01
 */
public final class DiningExperienceManager {
	
	/**
	 * The base cost for dining orders.
	 */
	private static final double BASE_COST = 5.0;

	/**
	 * The discount applied for ordering more than 5 meals.
	 */
	private static final double DISCOUNT_5_MEALS = 0.10;
	/**
	 * The discount applied for ordering more than 10 meals.
	 */
    private static final double DISCOUNT_10_MEALS = 0.20;
	/**
	 * The discount applied for ordering more than 50 meals.
	 */
    private static final double DISCOUNT_OVER_50 = 10.0;
	/**
	 * The discount applied for ordering more than 100 meals.
	 */
    private static final double DISCOUNT_OVER_100 = 25.0;
	/**
	 * The discount applies to orders equal to 50 meals.
	 */
    private static final double DISCOUNT_TD_50 = 50.0;
	/**
	 * The discount applies to orders equal to 100 meals.
	 */
    private static final double DISCOUNT_TD_100 = 100.0;
	/**
	 * The discount applies to orders equal to 10 meals.
	 */
    private static final double DISCOUNT_TD_10 = 10.0;
	/**
	 * The discount applies to orders equal to 5 meals.
	 */
    private static final double DISCOUNT_TD_5 = 5.0;
	/**
	 * Logger.
	 */
    private static final Logger logger = Logger.getLogger(DiningExperienceManager.class.getName());
    private DiningExperienceManager() {
        // Constructor privado para evitar la instanciación
    }
    /**
     * This is the main method that serves as the entry point of the program.
     *
     * @param args The command-line arguments passed to the program.
     */
    public static void main(String[] args) {
        final Map<String, Double> menu = createMenu();
        final Map<String, Integer> order = new ConcurrentHashMap<>();
        double totalCost = BASE_COST;

        try (Scanner scanner = new Scanner(System.in)) {
            logger.info("Welcome to Dining Experience Manager!");
            displayMenu(menu);

            while (true) {
                logger.info("Enter a meal from the menu (or 'done' to confirm): ");
                final String meal = scanner.nextLine().trim().toLowerCase(Locale.ENGLISH);
                final String done = "done";
                if (done.equals(meal)) {
                    break;
                }

                if (!menu.containsKey(meal)) {
                    logger.info("Invalid meal selection. Please select a meal from the menu.");
                    continue;
                }
                if (logger.isLoggable(Level.INFO)) {
                    logger.info("Enter the quantity for " + meal + ": ");
                }
                final int quantity = getValidQuantity(scanner);

                if (quantity > 0 && quantity <= 100) {
                    order.put(meal, quantity);
                    totalCost += menu.get(meal) * quantity;
                } else {
                    logger.info("Invalid quantity. Please enter a positive integer between 1 and 100.");
                }
            }
        }

        displayOrder(order);
        final double discountedCost = applyDiscounts(order, totalCost);

        if (discountedCost >= 0) {
            if (logger.isLoggable(Level.INFO)) {
                logger.info("Total cost: $" + (int) discountedCost);
            }
        } else {
            logger.info("Order canceled or invalid input. Returning -1.");
        }
    }

    private static Map<String, Double> createMenu() {
        final Map<String, Double> menu = new ConcurrentHashMap<>();
        menu.put("meal1", 10.0);
        menu.put("meal2", 15.0);
        menu.put("meal3", 20.0);
        // Add more meals and prices as needed
        return menu;
    }
    private static int getValidQuantity(final Scanner scanner) {
        int quantity;
        while (true) {
            try {
                quantity = Integer.parseInt(scanner.nextLine());
                if (quantity > 0) {
                    return quantity;
                } else {
                    logger.info("Please enter a positive integer greater than zero.");
                }
            } catch (NumberFormatException e) {
                logger.info("Invalid input. Please enter a positive integer greater than zero.");
            }
        }
    }

    private static void displayOrder(final Map<String, Integer> order) {
        logger.info("Selected Meals:");
        for (final Map.Entry<String, Integer> entry : order.entrySet()) {
            if (logger.isLoggable(Level.INFO)) {
                logger.info(entry.getKey() + " x" + entry.getValue());
            }
        }
    }
    private static void logMenu(final Map<String, Double> menu) {
        for (final Map.Entry<String, Double> entry : menu.entrySet()) {
            if (logger.isLoggable(Level.INFO)) {
                logger.info(entry.getKey() + ": $" + entry.getValue());
            }
        }
    }

    private static void displayMenu(final Map<String, Double> menu) {
        logMenu(menu);
    }

    private static double applyDiscounts(final Map<String, Integer> order, final double totalCost) {
        final int totalQuantity = order.values().stream().mapToInt(Integer::intValue).sum();
        double discountedCost = totalCost;

        if (totalQuantity > DISCOUNT_TD_5) {
            discountedCost *= 1 - DISCOUNT_5_MEALS;
        }

        if (totalQuantity > DISCOUNT_TD_10) {
            discountedCost *= 1 - DISCOUNT_10_MEALS;
        }

        if (discountedCost > DISCOUNT_TD_100) {
            discountedCost -= DISCOUNT_OVER_100;
        } else if (discountedCost > DISCOUNT_TD_50) {
            discountedCost -= DISCOUNT_OVER_50;
        }

        return discountedCost;
    }
}
