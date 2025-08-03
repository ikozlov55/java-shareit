package ru.practicum.shareit.testutils;

@SuppressWarnings("RegexpSinglelineJava")
public final class MocksData {
    private MocksData() {
    }

    public static String BOOKING = """
            {
                "id": 27,
                "item": {
                    "id": 45,
                    "name": "2CxK9TsesV",
                    "description": "I8L2FoTRyCOb6JbSvm6ERtIlDiI7m5WbqA84ktTdH41UCcQAnj",
                    "available": true,
                    "requestId": null,
                    "requestId": null,
                    "lastBooking": null,
                    "nextBooking": null,
                    "comments": []
                },
                "booker": {
                    "id": 125,
                    "name": "Clint Ritchie",
                    "email": "Domenick5@hotmail.com"
                },
                "start": "2025-07-30T18:51:40",
                "end": "2025-07-31T18:51:40",
                "status": "WAITING"
            }
            """;

    public static String ITEM = """
            {
                "id": 46,
                "name": "U9Grptyari",
                "description": "mJNNioPC7H6XuhCArpTXJpw1vaPcESTXuYpgIBoaNO4l79Uwss",
                "available": true,
                "requestId": null,
                "lastBooking": null,
                "nextBooking": null,
                "comments": []
            }
            """;
    public static String COMMENT = """
            {
                "id": 6,
                "text": "9iga6TvfTDnqTaPXlgoTKq5mDlOjc2bA3uHioIbb4l3aV3boZU",
                "authorName": "Stacy Jacobs",
                "created": "2025-07-30T20:11:53.0641433"
            }
            """;

    public static String ITEM_REQUEST = """
            {
                "id": 17,
                "description": "yzS12spuCaIdJRFSLFhB1ALt21VusxiExvuCwnXnsRQy29CaGO",
                "created": "2025-07-31T19:10:51.6964254",
                "items": null
            }
            """;
    public static String USER = """
            {
                "id": 137,
                "name": "Spencer Bosco",
                "email": "Felipe.Dibbert6@hotmail.com"
            }
            """;
}
