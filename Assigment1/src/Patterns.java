// ===== SINGLETON PATTERN =====
public class Patterns {
    // Private constructor to prevent instantiation from outside
    private Patterns() {
    }

    // Static inner class for lazy initialization and thread-safety
    // This approach is known as the Bill Pugh Singleton pattern
    private static class SingletonHolder {
        private static final Patterns INSTANCE = new Patterns();
    }

    // Public method to get the singleton instance
    public static Patterns getInstance() {
        return SingletonHolder.INSTANCE;
    }
}

// ===== FACTORY PATTERN =====

// Shape interface
interface Shape {
    void draw();
    double getArea();
}

// Concrete implementation - Circle
class Circle implements Shape {
    private double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    @Override
    public void draw() {
        System.out.println("Drawing Circle with radius: " + radius);
    }

    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }
}

// Concrete implementation - Square
class Square implements Shape {
    private double side;

    public Square(double side) {
        this.side = side;
    }

    @Override
    public void draw() {
        System.out.println("Drawing Square with side: " + side);
    }

    @Override
    public double getArea() {
        return side * side;
    }
}

// Concrete implementation - Rectangle
class Rectangle implements Shape {
    private final double width;
    private final double height;

    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw() {
        System.out.println("Drawing Rectangle with width: " + width + ", height: " + height);
    }

    @Override
    public double getArea() {
        return width * height;
    }
}

// Factory class
class ShapeFactory {
    public static Shape createShape(String shapeType, double... dimensions) {
        if (shapeType == null) {
            return null;
        }

        switch (shapeType.toLowerCase()) {
            case "circle":
                if (dimensions.length > 0) {
                    return new Circle(dimensions[0]);
                }
                break;
            case "square":
                if (dimensions.length > 0) {
                    return new Square(dimensions[0]);
                }
                break;
            case "rectangle":
                if (dimensions.length >= 2) {
                    return new Rectangle(dimensions[0], dimensions[1]);
                }
                break;
            default:
                System.out.println("Unknown shape type: " + shapeType);
        }
        return null;
    }
}

