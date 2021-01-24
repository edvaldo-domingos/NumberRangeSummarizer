package numberrangesummarizer;

import java.util.*;
import java.util.stream.Stream;

public class Summarizer implements  NumberRangeSummarizer{
    private String ranges = "";

    @Override
    public Collection<Integer> collect(String input) throws InvalidInputException {
        Collection sortedNumbers;
        input = validateInput(input);
        input = removeNonNumericCharacters(input);
        sortedNumbers = sortNumbersAndCreateCollection(input);

        return  sortedNumbers;
    }

    private String validateInput(String input) throws InvalidInputException {
        // if null
        if(input == null)
            throw new NullPointerException("Invalid input. Please do not pass a null string");

        // empty input
        if(input.isEmpty())
            throw new InvalidInputException("Invalid input. Please do not pass an empty string");

        //if not string
        if(!(input instanceof  String)){
            throw new NumberFormatException("Invalid input format. Input should be a String");
        }

        return input;
    }

    private String removeNonNumericCharacters(String input){
        // remove special all special characters and only leaves numeric values (including negative numbers)
        input = input.replaceAll("[^\\d,.-]", "")
                .trim()
                .replaceAll(",-,",","); // in case there was a negative sign without a value

        return input;
    }

    private Collection<Integer> sortNumbersAndCreateCollection(String formattedInput){
        Collection<Integer> sortedNumbers = new ArrayList<>();

        Stream.of(formattedInput.split(","))
                .filter(item -> isNumeric(item)
                                && !item.isEmpty()
                                && !item.isBlank()
                                && !item.contains(".") //skip fractional
                                && !item.equals("-")

                ) //filter out decimal values
                .map(String::trim)
                .map(Integer::parseInt)
                .distinct() // removes duplicates
                .sorted()
                .forEach(sortedNumbers::add);

                return sortedNumbers;
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    @Override
    public String summarizeCollection(Collection<Integer> input) throws InvalidInputException {

        if(isCollectionInputValid(input)){
            String rangesFound = findRanges(input);
            setRanges(rangesFound);

            return getRanges();
        }

        return  null;
    }

    private boolean isCollectionInputValid(Collection<Integer> input) throws InvalidInputException {
        // if null
        if(input == null)
            throw new NullPointerException("Collection cannot be null");

        // empty input
        if(input.isEmpty())
            throw new InvalidInputException("Collection cannot be empty");

        //if not a collection
        if(!(input instanceof  Collection)){
            throw new NumberFormatException("Ony Collection<Integer> is allowed");
        }

        return true;
    }

    private String findRanges(Collection<Integer> masterColl){
        ArrayList<Integer> values= new ArrayList<>(masterColl);
        String output ="";
        boolean isSequential = false;
        int rangeStart = 0;
        int rangeEnd = 0;

        for (int i = 0; i < values.size(); i++) {

            int nextNum ;
            int targetSum = values.get(i)  + 1;
            int currentValue = values.get(i);
            boolean isLast = i + 1 == values.size();


            // if one of the values inside the collection is null
            if(values.get(i) == null || !isLast && values.get(i + 1) == null){
                throw new NullPointerException("One of the values in the collection is null");
            }

            // Last item
            nextNum = isLast ? values.get(values.size() -1) : values.get(i + 1);

            if(nextNum == targetSum && isSequential){
                continue;
            }

            //add range start and the next sum is sequential
            if (nextNum == targetSum && !isSequential){
                rangeStart = currentValue;
                isSequential = true;
                continue;
            }

            // reached range end, next number is not sequential
            if(nextNum != targetSum && isSequential){
                //update the value of the map based on the key which is the start range
                isSequential = false;
                rangeEnd = currentValue;

                // add the range to the output variable
                output = output.concat(String.valueOf(rangeStart ))
                        .concat("-")
                        .concat(String.valueOf(rangeEnd ));

                // if it is not the last item add "," to the output variable
                if(!isLast)
                    output = output.concat(", ");

                rangeStart = 0;
                rangeEnd = 0;

                continue;
            }

            // in this case the next number is not sequential
            if(nextNum != targetSum && !isSequential){
                // in this case we don't have a range start neither the sum != current
                output = output.concat(String.valueOf(currentValue ));

                // if it is not the last item add "," to the output variable
                if(!isLast)
                    output = output.concat(", ");
            }

        }

        return  output;
    }

    private String getRanges(){
        return ranges;
    }

    private void  setRanges(String ranges){
        this.ranges = ranges;
    }

}
