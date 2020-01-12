package cn.edu.scau.acm.acmer.Doc;

import com.power.doc.builder.ApiDocBuilder;
import com.power.doc.constants.DocLanguage;
import com.power.doc.model.ApiConfig;
import org.junit.jupiter.api.Test;

public class DocService {

    @Test
    public void testBuilderControllersApi() {
        ApiConfig config = new ApiConfig();
        //当把AllInOne设置为true时，Smart-doc将会把所有接口生成到一个Markdown、HHTML或者AsciiDoc中
        config.setAllInOne(false);
        //Set the api document output path.
        config.setOutPath("d:\\md");

        config.setLanguage(DocLanguage.ENGLISH);


        //生成Markdown文件
        ApiDocBuilder.builderControllersApi(config);

    }

}
