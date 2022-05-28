package com.rau.narxozbeta

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Credentials
import android.os.*
import android.os.Message
import android.os.StrictMode.VmPolicy
import android.service.textservice.SpellCheckerService
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*
import javax.activation.DataHandler
import javax.activation.DataSource
import javax.activation.FileDataSource
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart


class Welcome : AppCompatActivity() {

    private var Name: String? = null
    private var course: String? = null
    private var course_result: String? = null
    private var speciality: String? = null
    private var start_year: String? = null
    private var end_year: String? = null
    private var study_form: String? = null
    private var study_duration: String? = null
    private var email: String? = null
    private var mFilePath: String? = null
    private var endyear: String? = null
    private var startyear: String? = null
    private var id: String? = null
    lateinit var appExecutors:AppExecutors
    val REQUEST_ID_MULTIPLE_PERMISSIONS = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        supportActionBar!!.hide()
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        val intent: Intent = getIntent();
        Name = intent.getStringExtra("message_key")
        course = intent.getStringExtra("course")
        course_result = course?.get(0).toString()
        speciality = intent.getStringExtra("facultati")
        start_year = intent.getStringExtra("start_year")
        end_year = intent.getStringExtra("end_year")
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        getId("https://narxozapp.000webhostapp.com/getid.php")

        appExecutors = AppExecutors()

        val spravki = resources.getStringArray(R.array.spravki)

        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_menu, spravki)

        val autocompleteTV = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView5)

        autocompleteTV.setAdapter(arrayAdapter)
        autocompleteTV.setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, position, _ ->

            val selectedValue = arrayAdapter.getItem(position)
            when {
                selectedValue.equals("Справка об обучении в колледже") -> {

                    savePdf1()
                    Handler().postDelayed({sendEmailNew("$Name.pdf")},1200)
                    val spravki1 = resources.getStringArray(R.array.spravki1)
                    val arrayAdapter1 = ArrayAdapter(this, R.layout.dropdown_menu, spravki1)
                    // get reference to the autocomplete text view
                    val autocompleteTV1 =
                        findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView5)
                    // set adapter to the autocomplete tv to the arrayAdapter
                    autocompleteTV1.setAdapter(arrayAdapter1)
                    Toast.makeText(
                        this,
                        "Справку можно брать только один раз чтобы взять еще раз перезайдите в приложение ",
                        Toast.LENGTH_LONG
                    ).show()
                    Handler().postDelayed({
                        deletefile("$Name.pdf")
                    }, 100000)
                }
                selectedValue.equals("Приложение 6") -> {
                    savepdf2()
                    Handler().postDelayed({sendEmailNew("$Name.pdf")},1200)
                    sendEmailNew("$Name.pdf")
                    val spravki1 = resources.getStringArray(R.array.spravki1)
                    val arrayAdapter1 = ArrayAdapter(this, R.layout.dropdown_menu, spravki1)
                    // get reference to the autocomplete text view
                    val autocompleteTV1 =
                        findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView5)
                    // set adapter to the autocomplete tv to the arrayAdapter
                    autocompleteTV1.setAdapter(arrayAdapter1)
                    Toast.makeText(
                        this,
                        "Справку можно брать только один раз чтобы взять еще раз перезайдите в приложение ",
                        Toast.LENGTH_LONG
                    ).show()
                    Handler().postDelayed({
                        deletefile("$Name.pdf")
                    }, 10000)
                }
                else -> {
                    savepdf3()
                    Handler().postDelayed({sendEmailNew("$Name.pdf")},1200)
                    sendEmailNew("$Name.pdf")
                    val spravki1 = resources.getStringArray(R.array.spravki1)
                    val arrayAdapter1 = ArrayAdapter(this, R.layout.dropdown_menu, spravki1)
                    // get reference to the autocomplete text view
                    val autocompleteTV1 =
                        findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView5)
                    // set adapter to the autocomplete tv to the arrayAdapter
                    autocompleteTV1.setAdapter(arrayAdapter1)
                    Toast.makeText(
                        this,
                        "Справку можно брать только один раз чтобы взять еще раз перезайдите в приложение ",
                        Toast.LENGTH_LONG
                    ).show()
                    Handler().postDelayed({
                        deletefile("$Name.pdf")
                    }, 10000)
                }
            }
        })


        /*button1.setOnClickListener{
            savePdf1()
        }
        button2.setOnClickListener{
            savepdf2()
        }
        button3.setOnClickListener{
            savepdf3()
        }*/
    }


    private fun savePdf1() {
        val mDoc = Document()

        val mFileName = "$Name.pdf"
        val mFilePath =
            this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                .toString() + "/" + mFileName
        try {
            //create instance of PdfWriter class
            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))
            //open the document for writing
            mDoc.open()
            mDoc.pageSize = PageSize.A4
            val valueFontSize = 10.0F
            val fontNameBold = BaseFont.createFont(
                "assets/fonts/times_new_roman_bold.ttf", "CP1251",
                BaseFont.EMBEDDED
            )
            val fontNameClassic = BaseFont.createFont(
                "assets/fonts/times_new_roman.ttf", "CP1251",
                BaseFont.EMBEDDED
            )

//add author of the document (metadata)
            mDoc.addAuthor("Narxoz")
//add title to Document


            val bm = BitmapFactory.decodeResource(resources, R.drawable.narxoz)
            val stream = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.PNG, 100, stream)
            var img: Image? = null
            val byteArray = stream.toByteArray()
            img = Image.getInstance(byteArray)
            img.scaleAbsolute(492f, 210.75f)
            mDoc.add(img)

           

            val titleStyle = Font(fontNameBold, valueFontSize, Font.NORMAL, BaseColor.BLACK)
            addItem(mDoc, "СПРАВКА", Element.ALIGN_CENTER, titleStyle)
//add values
            val valueStyle = Font(fontNameClassic, valueFontSize, Font.NORMAL, BaseColor.BLACK)
            val footer = Font(fontNameClassic, 8f, Font.NORMAL, BaseColor.BLACK)
            addItem(mDoc, "10-03 № ${id}", Element.ALIGN_LEFT, valueStyle)
            addItem(
                mDoc,
                "Дана ${Name} в том, что он/она действительно является студентом ${course_result} курса очной формы обучения по специальности «${speciality}».\n" +
                        "\t   Справка дана для предъявления по месту требования.\n",
                Element.ALIGN_MIDDLE,
                valueStyle
            )
            addItem(mDoc, "     ", Element.ALIGN_MIDDLE, valueStyle)
           
            addItem(mDoc, "    ", Element.ALIGN_MIDDLE, valueStyle)
            addItem(mDoc, "    ", Element.ALIGN_MIDDLE, valueStyle)
            addItem(
                mDoc, "Исполнитель: Байтілес Г.М.\n" +
                        "тел. 391-10-41, факс. 303-02-27\n", Element.ALIGN_MIDDLE, footer
            )
            addItem(mDoc, "    ", Element.ALIGN_MIDDLE, valueStyle)
           

           
            mDoc.close()
            //show file saved message with file name and path

            
        } catch (e: Exception) {
            Log.d("MyLog", "Error " + e)
        }
    }

    fun savepdf2() {
        val mDoc = Document()

        val mFileName = "$Name.pdf"
        val mFilePath =
            this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                .toString() + "/" + mFileName
        try {
            //create instance of PdfWriter class
            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))
            //open the document for writing
            mDoc.open()
            mDoc.pageSize = PageSize.A4
            val valueFontSize = 10.0F
            val fontNameBold = BaseFont.createFont(
                "assets/fonts/times_new_roman_bold.ttf", "CP1251",
                BaseFont.EMBEDDED
            )
            val fontNameClassic = BaseFont.createFont(
                "assets/fonts/times_new_roman.ttf", "CP1251",
                BaseFont.EMBEDDED
            )
            val valueStyle = Font(fontNameClassic, valueFontSize, Font.NORMAL, BaseColor.BLACK)
            val footer = Font(fontNameClassic, 8f, Font.NORMAL, BaseColor.BLACK)

            mDoc.addAuthor("Narxoz")



            addItem(
                mDoc,
                "Приложение 6\n" +
                        "к Правилам предоставления государственной" + "\nбазовой пенсионной выплаты за счет бюджетных" + "\nсредств, а также назначения и осуществления" + "\nпенсионных выплат по возрасту, государственных социальных пособий по инвалидности, по случаю\n" +
                        "потери кормильца, государственных специальных пособий\n",
                Element.ALIGN_RIGHT,
                valueStyle
            )
            val titleStyle = Font(fontNameBold, valueFontSize, Font.NORMAL, BaseColor.BLACK)
            addItem(mDoc, "СПРАВКА", Element.ALIGN_CENTER, titleStyle)
            addItem(
                mDoc,
                "Дана гражданину  \n" + " ${Name} " + " ,________________ (дата рождения) " +

                        "в том, что он (а) действительно является обучающимся\n" +
                        "Учреждение «Экономический колледж университета Нархоз»                          \n" +

                        "Государственная лицензия  АА5 №0059670 от 07.10.2008г.  бессрочная            \n" +

                        " ${course_result} курса, форма обучения дневная,очная. \n" +
                        "\n" +
                        "Справка действительна на ${start_year}/${end_year} учебный год.\n" +
                        "Справка выдана для предъявления в                                         отделение Государственной корпорации.\n" +
                        "Срок обучения в учебном заведении" + " 2 года 10 месяцев,\n" +
                        "период обучения с " + " ${start_year}" + " по " + " ${end_year}\n" +
                        "\n" +
                        "Примечание: справка действительна 1 год.\n" +
                        "В случаях отчисления обучающегося из учебного заведения или перевода на заочную форму обучения, руководитель учебного заведения извещает Государственной корпорации по месту жительства получателя пособия.\n",
                Element.ALIGN_MIDDLE,
                valueStyle
            )
            addItem(mDoc, "    ", Element.ALIGN_MIDDLE, valueStyle)
           

           
            mDoc.close()
            //show file saved message with file name and path

            
        } catch (e: Exception) {
            Log.d("MyLog", "Error " + e)
        }
    }

    fun savepdf3() {
        val mDoc = Document()

        val mFileName = "$Name.pdf"
        val mFilePath =
            this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                .toString() + "/" + mFileName
        try {
            //create instance of PdfWriter class
            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))
            //open the document for writing
            mDoc.open()
            mDoc.pageSize = PageSize.A4
            val valueFontSize = 10.0F
            val fontNameBold = BaseFont.createFont(
                "assets/fonts/times_new_roman_bold.ttf", "CP1251",
                BaseFont.EMBEDDED
            )
            val fontNameClassic = BaseFont.createFont(
                "assets/fonts/times_new_roman.ttf", "CP1251",
                BaseFont.EMBEDDED
            )

//add author of the document (metadata)
            mDoc.addAuthor("Narxoz")


            val valueStyle = Font(fontNameClassic, valueFontSize, Font.NORMAL, BaseColor.BLACK)
            val footer = Font(fontNameClassic, 8f, Font.NORMAL, BaseColor.BLACK)
            val titleStyle = Font(fontNameBold, valueFontSize, Font.NORMAL, BaseColor.BLACK)
            addItem(
                mDoc,
                "Приложение 4" + "\nк Правилам назначения и осуществления" + "\nвыплаты государственных базовых социальных пособий по инвалидности,\n" +
                        "по случаю потери кормильца и по возрасту," + "\nпенсионных выплат из Государственного" + "\nцентра по выплате пенсий, государственной" + "\nбазовой пенсионной выплаты, государственных\n" +
                        "специальных пособий\n" +
                        "Форма\n",
                Element.ALIGN_RIGHT,
                valueStyle
            )
            addItem(
                mDoc,
                "Угловой штамп" + "\nучебного заведения" + "\nдата выдачи, исх. 10-03 № ${id}",
                Element.ALIGN_LEFT,
                valueStyle
            )
            addItem(mDoc, "СПРАВКА", Element.ALIGN_CENTER, titleStyle)
            addItem(
                mDoc,
                "\n" +
                        "Дана гражданину \n" + " ${Name} " + " _______________(дата рождения) " +

                        "\n" +
                        "в том, что он (а) действительно является обучающимся\n" +
                        "\n" +
                        "Экономического колледжа Университета Нархоз\n" +

                        "\n" +
                        "Государственная лицензия АА 5   № 0059670 от 07.10.2008 г. бессрочная \n" +

                        "\n" + " ${course_result}" +
                        " класса/курса, форма обучения дневная,очная.\n" +
                        "\n" +
                        "Справка действительна на ${start_year}/${end_year}   учебный год.\n" +
                        "\n" +
                        "Справка выдана для предъявления\n" +
                        "в                                            отделение Государственной корпорации\n" +
                        "Срок обучения в учебном заведении" + " 2 года 10 месяцев\n" +
                        "\n" +
                        "период обучения с " + " ${start_year}" + " по " + " ${end_year}\n" +
                        "\n" +
                        "\n" +
                        "Примечание: справка действительна 1 год.\n" +
                        "В случае отчисления обучающегося из учебного заведения или перевода на заочную форму обучения, руководитель учебного заведения извещает отделение Государственной Корпорации Правительство для граждан по месту жительства получателя пособия.\n",
                Element.ALIGN_MIDDLE,
                valueStyle
            )
            addItem(mDoc, "    ", Element.ALIGN_MIDDLE, valueStyle)
           

           
            mDoc.close()
            //show file saved message with file name and path

            
        } catch (e: Exception) {
            Log.d("MyLog", "Error " + e)
        }
    }

    @Throws(DocumentException::class)
    fun addItem(document: Document, text: String, align: Int, style: Font) {
        val chunk = Chunk(text, style)
        val p = Paragraph(chunk)
        p.alignment = align
        document.add(p)
    }



    fun sendEmail(filename: String) {

        try {
            val builder = VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())
            val filelocation = File(
                this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
                filename
            )
            val path = FileProvider.getUriForFile(
                this,
                this.getApplicationContext().getPackageName() + ".provider",
                filelocation
            )
            val emailIntent = Intent(Intent.ACTION_SEND).apply {
                // set the type to 'email'
                type = "vnd.android.cursor.dir/email"
                val to = arrayOf("spravka@college-narxoz.kz")
                putExtra(Intent.EXTRA_EMAIL, to)
                // the attachment
                putExtra(Intent.EXTRA_STREAM, path)
                // the mail subject
                
            }
            startActivity(Intent.createChooser(emailIntent, "Send email..."))

        } catch (e: Exception) {
            Log.d("MyLog", "is exception raises during sending mail" + " " + "$e")
        }
    }

    fun deletefile(filename: String) {
        File(
            this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
            filename
        ).delete()
    }

    fun sendEmailNew(filename: String) {
        val mFilePath =
            this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                .toString() + "/" + filename

        val javaMailApi = JavaMailAPI(this, "college gmail",filename,"Справка",mFilePath,filename)
        javaMailApi.execute()
    }
    fun getId(url: String) {
        val url: String = url;
        var requestQueue = Volley.newRequestQueue(this)
        var stringRequest = StringRequest(Request.Method.GET, url,

            { response ->

                val Jsonobject: JSONObject = JSONObject(response)
                id = Jsonobject.getString("id")
                Log.d("Mylog", "$id")



            },
            { error ->
                //Toast Error
                Toast.makeText(this, "Fail to get id" + error, Toast.LENGTH_SHORT).show();
            })

        requestQueue.add(stringRequest)
    }
}




