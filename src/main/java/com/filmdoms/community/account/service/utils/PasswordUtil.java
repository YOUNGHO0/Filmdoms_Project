package com.filmdoms.community.account.service.utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PasswordUtil {

    private static String upperCases = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String lowerCases = "abcdefghijklmnopqrstuvwxyz";
    private static String specials = "@$!%*#?&";
    private static String numbers = "0123456789";

    public static String generateRandomPassword(int passwordLength) {
        if (passwordLength < 8) { //변경 가능
            throw new IllegalArgumentException("최소 비밀번호 길이는 8입니다.");
        }

        SecureRandom random = new SecureRandom();
        List<Character> charList = new ArrayList<>();

        //대문자, 소문자, 특수문자, 숫자를 1/4씩 넣기
        int quarter = passwordLength / 4;
        IntStream.range(0, quarter)
                .forEach(i -> charList.add(extractRandomCharacter(upperCases, random)));
        IntStream.range(0, quarter)
                .forEach(i -> charList.add(extractRandomCharacter(lowerCases, random)));
        IntStream.range(0, quarter)
                .forEach(i -> charList.add(extractRandomCharacter(specials, random)));
        IntStream.range(0, passwordLength - 3 * quarter)
                .forEach(i -> charList.add(extractRandomCharacter(numbers, random)));

        //섞은 다음 합치기
        Collections.shuffle(charList);
        return charList.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    private static char extractRandomCharacter(String sequence, SecureRandom random) {
        return sequence.charAt(random.nextInt(sequence.length()));
    }
}
