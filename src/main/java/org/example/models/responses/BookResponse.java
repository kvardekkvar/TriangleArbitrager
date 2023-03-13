package org.example.models.responses;

import org.example.models.book_request.BookData;

import java.util.List;

public class BookResponse {
    private String channel;
    private List<BookData> data;

    public String getChannel() { return channel; }
    public void setChannel(String value) { this.channel = value; }

    public List<BookData> getData() { return data; }
    public void setData(List<BookData> value) { this.data = value; }

}
