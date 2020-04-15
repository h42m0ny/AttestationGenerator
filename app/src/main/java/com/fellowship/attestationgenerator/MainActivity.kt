package com.fellowship.attestationgenerator

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {
    private var editTextFirstName: EditText? = null;
    private  var editTextLastName:EditText? = null;
    private  var editTextBirthday:EditText? = null;
    private var editTextLieuNaissance: EditText? = null;
    private  var editTextAddress:EditText? = null;
    private  var editTextTown:EditText? = null;
    private  var editTextZipCode:EditText? = null;
    private  var editTextDateSortie:EditText? = null;
    private  var editTextTime:EditText? = null;

    private var buttonSubmit: Button? = null;

    private var cbTravail: CheckBox? = null;
    private  var cbCourses:CheckBox? = null;
    private  var cbSante:CheckBox? = null;
    private  var cbFamille:CheckBox? = null;
    private  var cbSport:CheckBox? = null;
    private  var cbJudiciaire:CheckBox? =null;
    private  var cbMissions:CheckBox? = null;

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonSubmit = findViewById<View>(R.id.btn_send_mainactivity) as Button;
        editTextFirstName = findViewById<View>(R.id.firstname) as EditText;
        editTextLastName = findViewById<View>(R.id.lastname) as EditText;
        editTextBirthday = findViewById<View>(R.id.birthday) as EditText;
        editTextLieuNaissance = findViewById<View>(R.id.lieunaissance) as EditText;
        editTextAddress = findViewById<View>(R.id.address) as EditText;
        editTextTown = findViewById<View>(R.id.town) as EditText;
        editTextZipCode = findViewById<View>(R.id.zipcode) as EditText;
        editTextDateSortie = findViewById<View>(R.id.datesortie) as EditText;
        editTextTime = findViewById<View>(R.id.time) as EditText;
        initDateAndTime(editTextDateSortie!!, editTextTime!!);

        cbTravail = findViewById<View>(R.id.chk_travail) as CheckBox;
        cbCourses = findViewById<View>(R.id.chk_courses) as CheckBox;
        cbSante = findViewById<View>(R.id.chk_sante) as CheckBox;
        cbFamille = findViewById<View>(R.id.chk_famille) as CheckBox;
        cbSport = findViewById<View>(R.id.chk_sport) as CheckBox;
        cbJudiciaire = findViewById<View>(R.id.chk_judiciaire) as CheckBox;
        cbMissions = findViewById<View>(R.id.chk_missions) as CheckBox;


        cbTravail!!.setOnClickListener(View.OnClickListener {
            onCheckboxClicked(it);
        });
        cbCourses!!.setOnClickListener(View.OnClickListener {
            onCheckboxClicked(it);
        });
        cbSante!!.setOnClickListener(View.OnClickListener {
            onCheckboxClicked(it);
        });
        cbFamille!!.setOnClickListener(View.OnClickListener {
            onCheckboxClicked(it);
        });
        cbSport!!.setOnClickListener(View.OnClickListener {
            onCheckboxClicked(it);
        });
        cbJudiciaire!!.setOnClickListener(View.OnClickListener {
            onCheckboxClicked(it);
        });
        cbMissions!!.setOnClickListener(View.OnClickListener {
           onCheckboxClicked(it);
        });



        buttonSubmit!!.setOnClickListener {
            val intent = Intent(this@MainActivity, WebFillForm::class.java);
            val firstname = editTextFirstName!!.text.toString();
            val lastname = editTextLastName!!.text.toString();
            val birthday = editTextBirthday!!.text.toString();
            val lieunaissance = editTextLieuNaissance!!.text.toString();
            val address = editTextAddress!!.text.toString();
            val town = editTextTown!!.text.toString();
            val zipcode = editTextZipCode!!.text.toString();

            val datesortie = editTextDateSortie!!.text.toString();
            val time = editTextTime!!.text.toString();

            intent.putExtra("firstname", firstname);
            intent.putExtra("lastname", lastname);
            intent.putExtra("birthday", birthday);
            intent.putExtra("lieunaissance", lieunaissance);
            intent.putExtra("address", address);
            intent.putExtra("town", town);
            intent.putExtra("zipcode", zipcode);
            intent.putExtra("datesortie", datesortie);
            intent.putExtra("time", time);
            intent.putExtra("travail", this.cbTravail!!.isChecked);
            intent.putExtra("courses", this.cbCourses!!.isChecked);
            intent.putExtra("sante", this.cbSante!!.isChecked);
            intent.putExtra("famille", this.cbFamille!!.isChecked);
            intent.putExtra("sport", this.cbSport!!.isChecked);
            intent.putExtra("judiciaire", this.cbJudiciaire!!.isChecked);
            intent.putExtra("missions", this.cbMissions!!.isChecked);
            startActivity(intent);
        };
    }

    private fun onCheckboxClicked(view : View) {
        if (view is CheckBox ) {
            if(!view.isChecked){
                view.isChecked = true;
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initDateAndTime(editTxtDatesortie : EditText, editTxtTime : EditText) {
        val currDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        editTxtDatesortie.setText(currDate);
        val currTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        editTxtTime.setText(currTime);
    }
}

