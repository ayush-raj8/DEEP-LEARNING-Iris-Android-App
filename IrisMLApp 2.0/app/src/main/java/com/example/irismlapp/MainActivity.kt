package com.example.irismlapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.irismlapp.ml.Iris
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var button: Button = findViewById(R.id.button);
        button.setOnClickListener(View.OnClickListener{

            try {

                var ed1: EditText = findViewById(R.id.editTextNumberDecimal);
                var ed2: EditText = findViewById(R.id.editTextNumberDecimal2);
                var ed3: EditText = findViewById(R.id.editTextNumberDecimal3);
                var ed4: EditText = findViewById(R.id.editTextNumberDecimal4);

                var v1: Float = ed1.text.toString().toFloat();
                var v2: Float = ed2.text.toString().toFloat();
                var v3: Float = ed3.text.toString().toFloat();
                var v4: Float = ed4.text.toString().toFloat();

                var byteBuffer: ByteBuffer = ByteBuffer.allocateDirect(4 * 4);
                byteBuffer.order(ByteOrder.nativeOrder())
                byteBuffer.putFloat(v1)
                byteBuffer.putFloat(v2)
                byteBuffer.putFloat(v3)
                byteBuffer.putFloat(v4)

                val model = Iris.newInstance(this)
                // Creates inputs for reference.
                val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 4), DataType.FLOAT32)
                inputFeature0.loadBuffer(byteBuffer)

                // Runs model inference and gets result.
                val outputs = model.process(inputFeature0)
                val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray

                val tv: TextView = findViewById(R.id.textView)
                tv.setText(
                    "Confidences % : \n"+ "Iris-sesota " + (outputFeature0[0]*100).toString() + "% \n" + "Iris-versicolor " + (outputFeature0[1]*100).toString() +
                            "% \n" + "Iris-virginica " + (outputFeature0[2]*100).toString() + "% \n"
                )
                val names =arrayOf<String>("Iris-sesota","Iris-versicolor","Iris-virginica")
                val tv2: TextView = findViewById(R.id.textView2)


                var pos =0
                val n1 = outputFeature0[0]*100
                val n2 = outputFeature0[1]*100
                val n3 = outputFeature0[2]*100

                if (n1 >= n2 && n1 >= n3)
                    pos=0
                else if (n2 >= n1 && n2 >= n3)
                    pos=1
                else
                    pos=2


                tv2.setText(names[pos])
                // Releases model resources if no longer used.
                model.close()


            }
            catch(e:Exception)
            {
                Toast.makeText(getApplicationContext(), "Enter all fields (numeric values only)", Toast.LENGTH_LONG).show();
            }


        })
        
    }
}