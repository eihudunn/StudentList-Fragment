package vn.edu.hust.studentman

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ListView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar

class StudentListFragment : Fragment() {
    private lateinit var studentAdapter: StudentAdapter
    private val students = mutableListOf(
        StudentModel("Nguyễn Văn An", "SV001"),
        StudentModel("Trần Thị Bảo", "SV002"),
        StudentModel("Lê Hoàng Cường", "SV003"),
        StudentModel("Phạm Thị Dung", "SV004"),
        StudentModel("Đỗ Minh Đức", "SV005"),
        StudentModel("Vũ Thị Hoa", "SV006"),
        StudentModel("Hoàng Văn Hải", "SV007"),
        StudentModel("Bùi Thị Hạnh", "SV008"),
        StudentModel("Đinh Văn Hùng", "SV009"),
        StudentModel("Nguyễn Thị Linh", "SV010"),
        StudentModel("Phạm Văn Long", "SV011"),
        StudentModel("Trần Thị Mai", "SV012"),
        StudentModel("Lê Thị Ngọc", "SV013"),
        StudentModel("Vũ Văn Nam", "SV014"),
        StudentModel("Hoàng Thị Phương", "SV015"),
        StudentModel("Đỗ Văn Quân", "SV016"),
        StudentModel("Nguyễn Thị Thu", "SV017"),
        StudentModel("Trần Văn Tài", "SV018"),
        StudentModel("Phạm Thị Tuyết", "SV019"),
        StudentModel("Lê Văn Vũ", "SV020")
    )

    private val args: StudentListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_student_list, container, false)

        val studentListView = view.findViewById<ListView>(R.id.list_view_students)
        studentAdapter = StudentAdapter(requireContext(), students, ::onEditStudent, ::onDeleteStudent)
        studentListView.adapter = studentAdapter

        registerForContextMenu(studentListView)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.add_new -> {
                        val student = StudentModel("", "")
                        val action = StudentListFragmentDirections.actionStudentListFragmentToAddEditStudentFragment(student)
                        findNavController().navigate(action)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        args.updatedStudent?.let { it: StudentModel ->
            students.add(0, it)
            studentAdapter.notifyDataSetChanged()
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        requireActivity().menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        return when (item.itemId) {
            R.id.edit -> {
                val action = StudentListFragmentDirections.actionStudentListFragmentToAddEditStudentFragment(students[info.position], info.position)
                findNavController().navigate(action)
                true
            }
            R.id.remove -> {
                val removedStudent = students.removeAt(info.position)
                studentAdapter.notifyDataSetChanged()
                Snackbar.make(requireView(), "Đã xóa sinh viên", Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        students.add(info.position, removedStudent)
                        studentAdapter.notifyDataSetChanged()
                    }.show()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun onEditStudent(student: StudentModel, position: Int) {
        val action = StudentListFragmentDirections.actionStudentListFragmentToAddEditStudentFragment(student, position)
        findNavController().navigate(action)
    }

    private fun onDeleteStudent(position: Int) {
        val removedStudent = students.removeAt(position)
        studentAdapter.notifyDataSetChanged()
        Snackbar.make(requireView(), "Đã xóa sinh viên", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                students.add(position, removedStudent)
                studentAdapter.notifyDataSetChanged()
            }.show()
    }
}