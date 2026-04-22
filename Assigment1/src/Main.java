//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
void main() {
//    Long result1 = Fibonacci.fib(0);
//    System.out.println(result1);
//    Long result2 = Fibonacci.fib(1);
//    System.out.println(result2);
//    Long result3 = Fibonacci.fib(10);
//    System.out.println(result3);
//
//    Shape circle = ShapeFactory.createShape("circle", 5.0);
//    Shape square = ShapeFactory.createShape("square", 4.0);
//    Shape rectangle = ShapeFactory.createShape("rectangle", 3.0, 5.0);
//
//    circle.draw();
//    System.out.println("Area: " + circle.getArea());

    Map<String, Integer> frequencies = FileUtils.countWordFrequency("/Users/ito/Study/GithubCopilot_SpringBoot/Assigment/copilot-spring/Assigment1/src/file.txt");
    for (Map.Entry<String, Integer> entry : frequencies.entrySet()) {
        System.out.println(entry.getKey() + ": " + entry.getValue());
    }
    

}
