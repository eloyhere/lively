package pers.eloyhere.lively.converter;

import jakarta.persistence.Converter;
import jakarta.persistence.AttributeConverter;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.SQLException;

@Converter
public class StringBlobConverter implements AttributeConverter<String, Blob> {

    @Override
    public Blob convertToDatabaseColumn(String string) {
      try {
        return new SerialBlob(string.getBytes(StandardCharsets.UTF_8));
      }catch (SQLException exception){
        throw new RuntimeException(exception);
      }
    }

    @Override
    public String convertToEntityAttribute(Blob blob) {
        try(
                InputStream stream = blob.getBinaryStream();
                InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)
        ){
          char[] buffer = new char[stream.available()];
          reader.read(buffer);
          return new String(buffer);
        }catch (IOException | SQLException exception){
          throw new RuntimeException(exception);
        }
    }
}
