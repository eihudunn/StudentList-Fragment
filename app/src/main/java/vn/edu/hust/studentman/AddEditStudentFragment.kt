package vn.edu.hust.studentman

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

class AddEditStudentFragment : Fragment() {
    private val args: AddEditStudentFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_edit_student, container, false)

        val editName = view.findViewById<EditText>(R.id.edit_student_name)
        val editId = view.findViewById<EditText>(R.id.edit_student_id)
        val saveButton = view.findViewById<Button>(R.id.button_save)

        val student = args.student
        editName.setText(student.studentName)
        editId.setText(student.studentId)

        saveButton.setOnClickListener {
            val updatedName = editName.text.toString()
            val updatedId = editId.text.toString()
            val updatedStudent = StudentModel(updatedName, updatedId)
            val action = AddEditStudentFragmentDirections.actionAddEditStudentFragmentToStudentListFragment(updatedStudent)
            findNavController().navigate(action)
        }

        return view
    }
}