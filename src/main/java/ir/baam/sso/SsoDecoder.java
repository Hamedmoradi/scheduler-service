package ir.baam.sso;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import feign.FeignException;
import feign.Response;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;

import java.io.IOException;
import java.lang.reflect.Type;

public class SsoDecoder extends SpringDecoder {

  public SsoDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
    super(messageConverters);
  }

  @Override
  public SsoDto decode(
      Response response, Type type) throws IOException, FeignException {
    Gson gson = new GsonBuilder().serializeNulls().create();
    SsoDto ssoDto = gson.fromJson(response.body().asReader(), SsoDto.class);
    return ssoDto;
  }
}