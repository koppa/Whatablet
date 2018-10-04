package com.graeb.whatablet

import android.app.Activity.RESULT_OK
import android.app.Fragment
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.*
import android.widget.ProgressBar
import android.widget.Toast

class WebFragment : Fragment() {
    private var webview: WebView? = null
    private var progressbar: ProgressBar? = null
    private var state: Bundle? = null
    private var mUploadMessage: ValueCallback<Array<Uri>>? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        state = savedInstanceState

        if (BuildConfig.DEBUG) {
            WebView.setWebContentsDebuggingEnabled(true)
        }

        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = inflater.inflate(R.layout.fragment_web, container, false)

        webview = v.findViewById(R.id.webview)
        progressbar = v.findViewById(R.id.progressBar)

        if (state == null) {
            setupWebView()
        } else {
            webview!!.restoreState(state)
        }

        v.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            private var mPreviousHeight: Int = 0

            override fun onGlobalLayout() {
                val newHeight = v.height
                if (mPreviousHeight != 0) {
                    if (mPreviousHeight > newHeight) {
                        onKeyboardShown()
                    } else if (mPreviousHeight < newHeight) {
                        onKeyboardHidden()
                    } else {
                        // No change
                    }
                }
                mPreviousHeight = newHeight
            }

        })

        // requesting audio permission for recording audio messages
        if (activity.checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            val PERMISSION_REQUEST_CODE = 42
            activity.requestPermissions(arrayOf(android.Manifest.permission.RECORD_AUDIO), PERMISSION_REQUEST_CODE)
            Toast.makeText(context, "requesting permission", Toast.LENGTH_SHORT)
                    .show()
        }

        return v
    }

    private fun onKeyboardShown() {
        // TODO react.js obfuscates class names in HTML, therefore they change often
//        webview!!.loadUrl("javascript:document.getElementsByClassName('pane-header').style.visibility='hidden'")
//        webview!!.loadUrl("javascript:document.getElementsByClassName('pane-header').style.padding=0px")
//        webview!!.loadUrl("javascript:document.getElementsByClassName('pane-header').style.height=0px")
    }


    private fun onKeyboardHidden() {
//        webview!!.loadUrl("javascript:document.getElementsByClassName('pane-header').style.visibility='shown'")
//        webview!!.loadUrl("javascript:document.getElementsByClassName('pane-header').style.padding=10px")
//        webview!!.loadUrl("javascript:document.getElementsByClassName('pane-header').style.height=59px")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webview!!.saveState(outState)
    }

    private fun setupWebView() {
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)

        // enable correct settings
        val settings = webview!!.settings
        settings.javaScriptEnabled = true
        settings.userAgentString = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.79 Safari/537.36"
        settings.domStorageEnabled = true

        // file upload
        settings.allowFileAccess = true

        val javaScriptInterface = JavaScriptInterface()
        webview!!.addJavascriptInterface(javaScriptInterface, "JSInterface")

        webview!!.webChromeClient = object : WebChromeClient() {
            internal var toast: Toast? = null

            override fun onShowFileChooser(webView: WebView, uploadMsg: ValueCallback<Array<Uri>>, fileChooserParams: WebChromeClient.FileChooserParams): Boolean {
                mUploadMessage = uploadMsg
                val i = Intent(Intent.ACTION_GET_CONTENT)
                i.addCategory(Intent.CATEGORY_OPENABLE)
                i.type = "image/*"
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE)
                return true
            }

            override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                val b = super.onConsoleMessage(consoleMessage)

                Log.e("web js", consoleMessage.message())

                return b
            }

            override fun onProgressChanged(view: WebView, newProgress: Int) {
                progressbar!!.progress = newProgress

                progressbar!!.setVisibility(if (newProgress < 100) View.VISIBLE else View.GONE);
            }

            override fun onPermissionRequest(request: PermissionRequest?) {
                request!!.grant(request.resources)
                Toast.makeText(context, "Received permission request", Toast.LENGTH_SHORT)
                        .show()
            }
        }

        webview!!.webViewClient = object : WebViewClient() {
            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                super.onReceivedError(view, request, error)

                Log.e("web", error.description.toString())
            }

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                if (request.url.host.contains("whatsapp")) {
                    return super.shouldOverrideUrlLoading(view, request)
                } else {
                    val i = Intent(Intent.ACTION_VIEW, request.url)
                    startActivity(i)
                    return true
                }
            }

            override fun onReceivedHttpError(view: WebView, request: WebResourceRequest, errorResponse: WebResourceResponse) {
                super.onReceivedHttpError(view, request, errorResponse)

                Log.e("web http", errorResponse.toString())
            }
        }

        webview!!.loadUrl("https://web.whatsapp.com/🌐/de")
    }

    inner class JavaScriptInterface {
        @JavascriptInterface
        fun onBackPressed() {
            activity.runOnUiThread { activity.onBackPressed() }
        }
    }

    override fun onResume() {
        super.onResume()

        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener(View.OnKeyListener { view, i, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_BACK) {
                // Buttons have class icon-x-viewer
                webview!!.loadUrl(
                        "javascript:(function() {" +
                                "var bs = document.getElementsByClassName('icon-x-viewer');" +
                                " if (bs.length != 1) { window.JSInterface.onBackPressed(); };" +
                                "var b = bs[0];" +
                                "b.click();" +
                                "})()"
                )
                return@OnKeyListener true
            }
            false
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode == FILECHOOSER_RESULTCODE
                && mUploadMessage != null
                && intent != null
                && resultCode == RESULT_OK) {
            mUploadMessage!!.onReceiveValue(arrayOf<Uri>(intent.data))
        }
    }

    companion object {
        private val FILECHOOSER_RESULTCODE = 1
    }
}
