package online.fadai.api.aliyun.oss.service;

import org.springframework.web.multipart.MultipartFile;

public interface OSSService {
    /**
     * 上传文件，传递MultipartFile对象，返回oss的url地址
     */
    String uploadFile(MultipartFile multipartFile);
}
