/**
 * The AKQAFormServlet program implements an application that
 * converts the user input number as words which is then displayed on the webpage.
 *
 * @author  Manpreet Kaur
 * @version 1.0
 * @since   2021-12-08 
 */
package com.adobe.aem.guides.wknd.spa.react.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@Component(immediate = true, service = Servlet.class, property = { "sling.servlet.methods=" + HttpConstants.METHOD_POST,
        "sling.servlet.paths=" + "/bin/akqaform" })
@ServiceDescription("AKQA Form Servlet")
public class AKQAFormServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggerFactory.getLogger(AKQAFormServlet.class);

    /**
     * This method is the starting point of this servlet which reads the
     * number parameter from request and send the response as words.
     * @param request This is the sling HTTP object
     * @param response  This is the sling HTTP response.
     */
    @Override
    protected void doPost(final SlingHttpServletRequest request,
            final SlingHttpServletResponse response) throws ServletException, IOException {
        log.info("Invoking akqa form servlet.");
        StringBuffer input = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                input.append(line);
        } catch (Exception e) {}

        JsonParser parser = new JsonParser();
        if (input.length() > 0) {
        JsonElement jsonElement = parser.parse(input.toString());
        JsonObject rootObject = jsonElement.getAsJsonObject();
        int number = rootObject.get("number").getAsInt(); 
        String words = numberToWord(number);
        //response.setContentType("text/plain");
        //response.getWriter().write(words);
          PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            Gson g = new Gson();  
            out.print(g.toJson(words));
            out.flush();  
        }
    }

    /**
     * This method converts number to words.
     * @param number This is the number added as input by the user
     * @return words This is the word representation of the entered number.
     */
    protected String numberToWord(int number) {  
        String words = "";
        String unitsArray[] = { "zero", "one", "two", "three", "four", "five", "six", 
                "seven", "eight", "nine", "ten", "eleven", "twelve",
                "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", 
                "eighteen", "nineteen" };
        String tensArray[] = { "zero", "ten", "twenty", "thirty", "forty", "fifty",
                "sixty", "seventy", "eighty", "ninety" };

        if (number == 0) {
            return "zero";
        }
        // add minus before conversion if the number is less than 0
        if (number < 0) { 
            // convert the number to a string
            String numberStr = "" + number; 
            // remove minus before the number 
            numberStr = numberStr.substring(1); 
            // add minus before the number and convert the rest of number 
            return "minus " + numberToWord(Integer.parseInt(numberStr)); 
        } 
        // check if number is divisible by 1 million
        if ((number / 1000000) > 0) {
            words += numberToWord(number / 1000000) + " million ";
            number %= 1000000;
        }
        // check if number is divisible by 1 thousand
        if ((number / 1000) > 0) {
            words += numberToWord(number / 1000) + " thousand ";
            number %= 1000;
        }
        // check if number is divisible by 1 hundred
        if ((number / 100) > 0) {
            words += numberToWord(number / 100) + " hundred ";
            number %= 100;
        }

        if (number > 0) {
            // check if number is within teens
            if (number < 20) { 
                // fetch the appropriate value from unit array
                words += unitsArray[number];
            } else { 
                // fetch the appropriate value from tens array
                words += tensArray[number / 10]; 
                if ((number % 10) > 0) {
                    words += "-" + unitsArray[number % 10];
                }  
            }
        }
        return words;
    }
}
