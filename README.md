# Maritime Tides - Port Świnoujście
<p><i>Aplikacja webowa poświęcona statystykom statków przypływajacych do portu w Świnoujsciu</i></p>

## Motywacje - dlaczego akurat statki?
<p align="justify">Strona internetowa stanowi centralne miejsce, gdzie można łatwo udostępniać informacje na temat aktualnych przypłynięć statków do portu w&nbspŚwionujściu. Podando pomaga w promocji portu na szerszą skalę, prezentując jego unikalne cechy, usługi i możliwości. Aplikacja charakteryzuje się responsywnością umożliwiając efektywne prowadzenie rejestru statków.</p>

## Główne funkcjonalności aplikacji
<p align='justify'>Kluczowe funkcje dla efektywnego zarządzania portem i umożliwiają łatwą komunikację z użytkownikami oraz bezpieczne i efektywne zarządzanie danymi.</p>
<ul>
  <li><b>Rejestracja użytkownika:</b> Użytkownicy mają możliwość rejestracji i identyfikacji za pomocą adresu e-mail i hasła. Bezpieczeństwo jest zwiększone poprzez hashowanie haseł.</li>
  <li><b>Zarządzanie meldunkami:</b> Zarejestrowani użytkownicy mają możliwość dodawania, modyfikowania oraz usuwania meldunków przypłynięć statków do portu. Meldunki statków są przechowywane w bazie danych na serwerze.</li>
  <li><b>Eksportowanie i importowanie rekordów:</b> Użytkownicy mają możliwość eksportowania i importowania rekordów do bazy danych. Dane mogą być pobierane z pliku JSON lub XML.</li>
</ul>

## Zastosowane technologie
<p align="justify"> Podstawą dla aplikacji jest język Java, a dokładnie implementacja OpenJDK w wersji 21.01 oraz szkielet programistyczny Spring.
Dodatkowo w&nbspcelu usprawnienia prac zostały wykorzystane biblioteki:</p>
<ul>
  <li>Hibernate</li>
  <li>Jakarta</li>
  <li>Jackson</li>
  <li>Lombok</li>
  <li>JWT</li>
</ul>
<p align='justify'>
Do zaimplementowania front-endu został użyty silnik szablonów Thymeleaf, biblioteka styli Bootstrap oraz język JavaScript. Jako serwer uruchomieniowy został wykorzystany pakiet XAMPP. Do przechowywania danych aplikacja wykorzystuje relacyjną bazę danych MySQL. Zaimplementowano również testy dla aplikacji za pośrednictwem Swagger API oraz JUnit.
</p>

## Zewnętrzne PORT API
<p align='justify'>
  Aplikacja korzysta z zewnetrznego interfejsu programistycznego (PORT API) w celu wyszukania informacji o porcie macierzystym przypływajacego statku. Żądanie do API bazuje na kodzie portu, który składa się z pięciu znaków. Warto zauważyć, że API nie posiada wszytskich istniejacych kodów w swojej bazie danych.
</p>

## Podsumowanie 
<p align='justify'>Wybór języka Java (Maven) oraz szkieletu programistycznego Spring Boot do tworzenia back-endu aplikacji webowych przynosi wiele zalet, takich jak stabilność, bezpieczeństwo, łatwość utrzymania i rozwoju, co przekłada się na popularność tego języka w świecie programowania aplikacji webowych. Szeroki wybór bibliotek oraz zależności, które usprawniają prace pozwalają na bardzo dokładnie dostosowanie aplikacji do potrzeb projektu. </p>

## Przykładowe widoki aplikacji
![s1](https://github.com/Grzesiek2kk/Statki_Java/assets/84547266/841141d0-63c6-40d1-827e-5c7839edf525)
<p align='center'><i>Widok strony głównej</i></p>

![s2](https://github.com/Grzesiek2kk/Statki_Java/assets/84547266/aad6a941-d46a-4b9b-ba60-a71cc677bbde)
<p align='center'><i>Widok strony logowania</i></p>

![s3](https://github.com/Grzesiek2kk/Statki_Java/assets/84547266/cf86337f-a2d6-4d6f-a886-8a2fc67d47be)
<p align='center'><i>Widok panelu administratora</i></p>

![s4](https://github.com/Grzesiek2kk/Statki_Java/assets/84547266/cbcdacca-1227-47c5-be3d-26c55f2a6959)
<p align='center'><i>Widok tabeli przypłynięć</i></p>

![s5](https://github.com/Grzesiek2kk/Statki_Java/assets/84547266/59f141ba-9429-46e9-8b56-ef9354ed3186)
<p align='center'><i>Widok formularza do dodawania nowego przypłynięcia statku</i></p>

![s6](https://github.com/Grzesiek2kk/Statki_Java/assets/84547266/77494567-a9f6-4f98-9f64-61205782dc74)
<p align='center'><i>Widok szczegółów statku, który przypłynął do portu</i></p>

<hr>
<i>The project was developed as part of credit for classes at Lublin University of Technology.</i>
