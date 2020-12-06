import org.apache.commons.io.IOUtils
import java.nio.charset.StandardCharsets
import javax.net.ssl.*
import java.io.*
import java.net.URL
import java.security.KeyStore

//get current flowfile
def flowFile = session.get()
if(!flowFile) return

//init parameters for https json post request from dynamucally added attributes of NIFI ExecuteScript processor 
def keyStoreFilename = keyStoreFilename.value
def keyStorePassword = keyStorePassword.value
def keyPassword = keyPassword.value
def keyStoreType = keyStoreType.value
def trustStoreFilename = trustStoreFilename.value
def trsutStorePassword = trustStorePassword.value
def trustStoreType = trustStoreType.value
def tlsProtocol = tlsProtocol.value
def url = httpsUrl.value
def jsonMessage = jsonMessage.value

try
{
  flowFile = session.write(flowFile,{inputStream,outputStream -> 
    //setup keystore
    def clientStore = KeyStore.getInstance(keyStoreType)//i
    clientStore.load(new FileInputStream(keyStoreFilename),keyStorePassword.toCharArray())
    def kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
    def kms = kmf.getKeyManagers()
    
    //setup truststore
    def trustStore = KeyStore.getInstance(trustStoreType)
    trustStore.load(new FileInputStream(trustStoreFilename),trustStorePassword.toCharArray())
    def tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
    tmf.init(trustStore)
    def tms = tmf.getTrustManagers()
    
    //init ssl context
    def sslContext = SSLContext.getInstance(tlsProtocol)
    sslContext.init(kms,tms,null)
    
    //init https connection
    def conn = (HttpsURLConnection)new URL(url).openConnection()
    
    //disble host name verification in client side (optional)
    conn.setHostnameVerifier(new HostnameVerifier() {
        @override
        public boolean verify(String s,SSLSession sslSession) 
        {
          return true
        }
    }
    //init ssl factory
    conn.setSSLSocketFactory(sslContext.getSocketFactory())
                      
    //generate https request
    conn.setRequestMethod("POST")
    conn.setDoOutput(true)
    conn.setRequestProperty("Content-Type","application/json")
    conn.getOutputStream().write(jsonMessage.getBytes("UTF-8")
                                 
    //process https response
    def responseCode = conn.getResponseCode()
    def responseBody = IOUtils.toString(conn.getInputStream(),StandardCharsets.UTF_8)
                                 
    //Write response to flowfile
    outputStream.write(responseBody.getBytes(StandardCharsets.UTF_8)
                              
  } as StreamCallback
  
  //transfer control to success 
  session.transfer(flowFile,REL_SUCCESS)
}
catch(ex)
{
  flowFile = session.putAttribute(flowFile,"script_error_message",ex.getMessage())
  flowFile = session.putAttribute(flowFile,"script_error",e.toString())
  //transfer control to failure
  session.transfer(flowFile,REL_FAILURE)                        
}



