package com.fellowship.attestationgenerator

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient


class WebFillForm : AppCompatActivity() {
    private val GENERATERURL = "https://media.interieur.gouv.fr/deplacement-covid-19/";
    private lateinit var webView : WebView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_fill_form);
        webView = findViewById<View>(R.id.attgenerator_webview) as WebView
        // SET SETTINGS
        setSettings();

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),1
            );
        }

        webView.loadUrl(this.GENERATERURL);
        webView.webChromeClient = WebChromeClient();
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                val firstname = intent.getStringExtra("firstname");
                println("firstname in webview :: $firstname");
                val lastname = intent.getStringExtra("lastname");
                val birthday = intent.getStringExtra("birthday");
                val lieunaissance = intent.getStringExtra("lieunaissance");
                val address = intent.getStringExtra("address");
                val town = intent.getStringExtra("town");
                val zipcode = intent.getStringExtra("zipcode");
                val datesortie = intent.getStringExtra("datesortie");
                val time = intent.getStringExtra("time");
                val travail = ((intent.extras!!.get("travail")).toString()).toBoolean();
                val courses = ((intent.extras!!.get("courses")).toString()).toBoolean();
                val sante = ((intent.extras!!.get("sante")).toString()).toBoolean();
                val famille = ((intent.extras!!.get("famille")).toString()).toBoolean();
                val sport = ((intent.extras!!.get("sport")).toString()).toBoolean();
                val judiciaire = ((intent.extras!!.get("judiciaire")).toString()).toBoolean();
                val missions = ((intent.extras!!.get("missions")).toString()).toBoolean();
                println("checkboxes in webview :: travail = $travail / courses = $courses / sante = $sante / famille = $famille / sport = $sport / judciaire = $judiciaire / missions = $missions");

                val js = "document.getElementById('field-firstname').value='" + firstname + "';" +
                        "document.getElementById('field-lastname').value='" + lastname + "';" +
                        "document.getElementById('field-birthday').value='" + birthday + "';" +
                        "document.getElementById('field-lieunaissance').value='" + lieunaissance + "';" +
                        "document.getElementById('field-address').value='" + address + "';" +
                        "document.getElementById('field-town').value='" + town + "';" +
                        "document.getElementById('field-zipcode').value='" + zipcode + "';" +
                        "document.getElementById('checkbox-travail').checked=" + travail + ";" +
                        "document.getElementById('checkbox-courses').checked=" + courses + ";" +
                        "document.getElementById('checkbox-sante').checked=" + sante + ";" +
                        "document.getElementById('checkbox-famille').checked=" + famille + ";" +
                        "document.getElementById('checkbox-sport').checked=" + sport + ";" +
                        "document.getElementById('checkbox-judiciaire').checked=" + judiciaire + ";" +
                        "document.getElementById('checkbox-missions').checked=" + missions + ";" +
                        "document.getElementById('field-datesortie').value='" + datesortie + "';" +
                        "document.getElementById('field-heuresortie').value='" + time + "';" +
                        "document.getElementById('generate-btn').click();";
                //view.loadUrl(js);
                println("javascript :: $js");
                view.evaluateJavascript(js, null);

            }
        };
        webView.addJavascriptInterface(JavaScriptInterface(applicationContext), "Android");
        webView.setDownloadListener { url, userAgent, contentDisposition, mimeType, contentLength ->
            webView.loadUrl(JavaScriptInterface.getBase64StringFromBlobUrl(url))
        }
    }

    private fun setSettings() {
        val settings = this.webView.settings;
        settings.javaScriptEnabled = true;
        settings.domStorageEnabled = true;
        settings.setAppCacheEnabled(true);
        settings.cacheMode = WebSettings.LOAD_DEFAULT;
        settings.setAppCachePath(cacheDir.path);
        settings.setSupportZoom(true);
        settings.builtInZoomControls = true;
        settings.displayZoomControls = true;
        // Zoom web view text
        settings.textZoom = 125;

        // Enable disable images in web view
        settings.blockNetworkImage = false
        // Whether the WebView should load image resources
        settings.loadsImagesAutomatically = true


        // More web view settings
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            settings.safeBrowsingEnabled = true
        }
        //settings.pluginState = WebSettings.PluginState.ON
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            settings.mediaPlaybackRequiresUserGesture = false
        }

        // More optional settings, you can enable it by yourself
        settings.setSupportMultipleWindows(true);
        settings.loadWithOverviewMode = true;
        settings.allowContentAccess = true;
        settings.setGeolocationEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.allowUniversalAccessFromFileURLs = true;
        }
        settings.allowFileAccess = true;

        // WebView settings
        this.webView.fitsSystemWindows = true;

        /* if SDK version is greater of 19 then activate hardware acceleration
        otherwise activate software acceleration  */
        this.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
    }
}

