package com.logn.yunupan.utils.Retrofit;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * 包装RequestBody  ， 添加进度监听器
 */

public class ProgressRequestBody extends RequestBody {
    //待包装请求体
    private RequestBody requestBody;
    //进度接口
    private ProgressListener.RequestListener requestListener;
    private BufferedSink bufferedSink;


    /**
     * 构造函数，赋值
     *
     * @param requestBody     待包装的请求体
     * @param requestListener 回调接口
     */
    public ProgressRequestBody(RequestBody requestBody, ProgressListener.RequestListener requestListener) {
        this.requestBody = requestBody;
        this.requestListener = requestListener;
    }

    /**
     * 重写，调用实际的请求体的 contextType()
     *
     * @return
     */
    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    /**
     * 重写，调用实际请求体的 contextLength()
     *
     * @return contentLength
     * @throws IOException 异常
     */
    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    /**
     * 重写，进行写入
     *
     * @param sink
     * @throws IOException
     */
    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null) {//包装
            bufferedSink = Okio.buffer(sink(sink));
        }
        //写入
        requestBody.writeTo(bufferedSink);
        //必须调用flush 否则最后一部分可能不会被写入
        bufferedSink.flush();
    }

    private Sink sink(BufferedSink sink) {
        return new ForwardingSink(sink) {
            //当前写入的字节数
            long bytesWritten = 0L;
            //总字节长度，避免多次调用contentLength() 方法
            long contentLength = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) {
                    contentLength = contentLength();
                }
                bytesWritten += byteCount;
                if (requestListener != null) {
                    requestListener.onRequest(bytesWritten, contentLength(), bytesWritten == contentLength);
                }
            }
        };
    }


}
