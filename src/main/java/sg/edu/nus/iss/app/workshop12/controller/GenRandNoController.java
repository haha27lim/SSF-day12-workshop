package sg.edu.nus.iss.app.workshop12.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sg.edu.nus.iss.app.workshop12.exception.RandNoException;
import sg.edu.nus.iss.app.workshop12.models.Generate;

@Controller
@RequestMapping(path="/rand")
public class GenRandNoController {
    
    /*
     * Redirect to the generate.html and show the input form
     */
    @GetMapping(path="/show")
    public String showRandForm(Model model) {
        // Instantiate the generate object
        // bind the noOfRandNo to the text field
        Generate g = new Generate();
        // associate the bind var to the view/page
        model.addAttribute("generateObj", g);
        return "generate";
    }

    /*
     * Generate the random number by GET request with the Query String request parameter numberVal
     */
    @GetMapping(path="/generate")
    public String generateRandNumByGet(@RequestParam Integer numberVal, Model model){
        this.randomizeNum(model, numberVal.intValue());
        return "result";
    }

    /*
     * Generate the random number by GET request with the path variable numberVal
     */
    @GetMapping(path="/generate/{numberVal}")
    public String generateRandNumByGetPV(@PathVariable Integer numberVal, Model model){
        this.randomizeNum(model, numberVal.intValue());
        return "result";
    }

    /*
     * Generate the random number by POST request
     */
    @PostMapping(path="/generate")
    public String postRandNum(@ModelAttribute Generate generate, Model model) {
        this.randomizeNum(model, generate.getNumberVal());
        return "result";
    }

    /*
     * Helper method for generating random numbers and adding them to the model for rendering in the view
     */
    private void randomizeNum (Model model, int noOfGenerateNo) {
        int maxGenNo = 30;
        String[] imgNumbers = new String [maxGenNo+1];

        // Validate only accept gt 0 lte 30
        if (noOfGenerateNo < 0 || noOfGenerateNo > maxGenNo) {
            throw new RandNoException();
        }

        // generate all the number images store it to the imgNumbers array
        for (int i = 0; i< maxGenNo +1; i++) {
            imgNumbers[i] = "number" + i + ".jpg";
        }

        // List for storing the selected random number images
        List<String> selectedImg = new ArrayList<String>();
        // Random number generator
        Random rnd = new Random();
        // Set for storing unique random numbers
        Set<Integer> uniqueResult = new LinkedHashSet<Integer>();
        // Generate unique random numbers until the desired number is reached
        while(uniqueResult.size() < noOfGenerateNo) {
            Integer resultOfRand = rnd.nextInt(maxGenNo);
            uniqueResult.add(resultOfRand);
        }

        // Iterate through the set of unique random numbers and add the corresponding number images to the list
        Iterator<Integer> it = uniqueResult.iterator();
        Integer currElem = null;
        while(it.hasNext()){
            currElem = it.next();
            selectedImg.add(imgNumbers[currElem.intValue()]);
        }

        // Add the number of random numbers and the array of random number images to the model
        model.addAttribute("numberRandomNum", noOfGenerateNo);
        model.addAttribute("randNumResult", selectedImg.toArray());
    }
}
