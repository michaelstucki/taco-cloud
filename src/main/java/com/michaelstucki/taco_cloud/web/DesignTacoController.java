package com.michaelstucki.taco_cloud.web;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.michaelstucki.taco_cloud.Ingredient;
import com.michaelstucki.taco_cloud.Ingredient.Type;
import com.michaelstucki.taco_cloud.Taco;
import com.michaelstucki.taco_cloud.TacoOrder;

// Generate a Simple Logging Facade for Java Logger (to be used later)
@Slf4j
// This class is a Controller, autoconfiguration will scan for it and create its bean
@Controller
// This class is mapped to an HTTP request to this endpoint (this path)
@RequestMapping("/design")
// Persist the TacoOrder bean for the duration of the client's session (bean scope)
@SessionAttributes("tacoOrder")
public class DesignTacoController {

    // Spring MVC adds the list of ingredients to the Model instance
    // A Model instance ferries data from the controller to the view
    // When the endpoint is called, this method is run
    @ModelAttribute
    public void addIngredientsToModel(Model model) {
        List<Ingredient> ingredients = Arrays.asList(
                new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
                new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
                new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
                new Ingredient("CARN", "Carnitas", Type.PROTEIN),
                new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
                new Ingredient("LETC", "Lettuce", Type.VEGGIES),
                new Ingredient("CHED", "Cheddar", Type.CHEESE),
                new Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
                new Ingredient("SLSA", "Salsa", Type.SAUCE),
                new Ingredient("SRCR", "Sour Cream", Type.SAUCE)
        );
        // Each ingredient Type is associated with a Collection ingredients of that Type
        Type[] types = Ingredient.Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
        }
    }

    // Thymeleaf uses Iterable in its th:each HTML tag attribute
    private Iterable<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
        return ingredients
                .stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }

    // Spring MVC adds an empty TacoOrder instance to the Model
    @ModelAttribute(name = "tacoOrder")
    public TacoOrder order() { return new TacoOrder(); }

    // Spring MVC adds an empty Taco instance to the Model
    @ModelAttribute(name = "taco")
    public Taco taco() { return new Taco(); }

    // A GET HTTP request to /design path results in this method being called
    // This method returns the logical name of the view (i.e., the design.html file)
    @GetMapping
    public String showDesignForm() { return "design"; }

    // A POST HTTP request to /design path results in this method being called
    // When design.html form is submitted:
    //  - its fields are bound to the properties of a Taco instance
    //  - The Taco instance is passed to this method
    // The design.html ingredients check boxes have textual (i.e., String) values (cf. th:value = ingredient.id)
    //  But the Taco object's ingredients instance variable is a collection of type Ingredient.
    //  So, we must convert from String to Ingredient; cf. IngredientByIdConverter class converter() method.
    //  Spring MVC will use converter() when conversion of request parameters (String) to bound properties (Ingredient)
    //  is needed.
    // @ModelAttribute: use the TacoOrder instance annotated in the above order() method
    @PostMapping
    public String processTaco(Taco taco, @ModelAttribute TacoOrder tacoOrder) {
        tacoOrder.addTaco(taco);
        log.info("Process tac: {}", taco);
        // Direct browser to go to /orders/current
        return "redirect:/orders/current";
    }
}
