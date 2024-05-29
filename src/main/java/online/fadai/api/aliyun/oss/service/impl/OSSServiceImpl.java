package online.fadai.api.aliyun.oss.service.impl;

import com.aliyun.oss.OSS;
import jakarta.annotation.Resource;
import online.fadai.api.aliyun.oss.config.OSSProperties;
import online.fadai.api.aliyun.oss.service.OSSService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OSSServiceImpl implements OSSService {
    @Resource
    private OSSProperties ossProperties;
    @Resource
    private OSS oss;
    @Override
    public String uploadFile(MultipartFile multipartFile){
        if (!multipartFile.isEmpty()) {
            String url = "";
            try {
                if (!multipartFile.isEmpty()) {
                    String ext = "unknown";
                    String filename = multipartFile.getOriginalFilename();
                    if (filename != null && filename.indexOf(".") > 0) {
                        ext = filename.substring(filename.indexOf(".") + 1);
                    }
                    String newFileName = UUID.randomUUID() + "." + ext;
                    InputStream inputStream = multipartFile.getInputStream();
                    oss.putObject(ossProperties.getBucket(),newFileName,inputStream);
                    url = "https://" + ossProperties.getBucket() + "." + ossProperties.getPath() + "/" + newFileName;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return url;
        }else {
            throw new RuntimeException("文件无效");
        }
    }
}
