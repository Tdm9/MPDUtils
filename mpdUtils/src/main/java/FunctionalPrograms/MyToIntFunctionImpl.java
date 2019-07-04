package FunctionalPrograms;



public class MyToIntFunctionImpl<T> {
    //@Test
    public void shouldSumlAlIntResults() throws NumberFormatException {
        MyToIntFunction<String> toIntF =
                String::length;
        MyToIntFunction<String> toIntF1 =
                s -> s.chars().allMatch(Character::isDigit)? Integer.parseInt(s) : 0;
        MyToIntFunction<String> toIntF2 =
                s -> s.chars().allMatch(Character::isDigit) ? Integer.parseInt(s) % 2 : 0;
        int sum = toIntF.sumAll(toIntF1, toIntF2).applyAsInt("123");
        //assertEquals(127, sum);
    }
}
