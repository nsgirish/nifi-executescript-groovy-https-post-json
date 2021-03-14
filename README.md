# HTTPS POST JSON request via NIFI ExecuteScript groovy processor

## Summary

Apache NIFI provides InovkeHttp processor to make HTTP / HTTPS requests. But if there are some customizations requried while making the HTTP(S) request such as disabling hostname verification, InvokeHttp processor doesn't support the same. So in those times the same functionality can be implemented with additional features via groovy scripting as illustrated in https_json_post.groovy
