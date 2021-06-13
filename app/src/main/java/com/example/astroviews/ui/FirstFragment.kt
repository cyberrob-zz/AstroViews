package com.example.astroviews.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.astroviews.R
import com.example.astroviews.databinding.FragmentFirstBinding
import com.example.astroviews.viewmodel.FirstViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private val firstViewModel: FirstViewModel by viewModel()

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            firstViewModel.retrieveData()
        }

        firstViewModel.state.observe(viewLifecycleOwner, { state ->

            when (state) {
                FirstViewModel.State.Failed -> {
                    binding.buttonFirst.isEnabled = false
                }
                FirstViewModel.State.NoData -> {
                    binding.buttonFirst.isEnabled = false
                }
                FirstViewModel.State.Parsing -> {
                    binding.buttonFirst.isEnabled = false
                }
                FirstViewModel.State.Retrieving -> {
                    binding.buttonFirst.isEnabled = false
                }
                FirstViewModel.State.Saving -> {
                    binding.buttonFirst.isEnabled = false
                }
                FirstViewModel.State.Succeed -> {
                    binding.buttonFirst.isEnabled = true
                    AlertDialog.Builder(requireContext())
                        .setTitle(state.message)
                        .setMessage("Check it out?")
                        .setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                            findNavController().navigate(R.id.action_FirstFragment_to_gridViewFragment)
                        }
                        .setNegativeButton(
                            "Cancel"
                        ) { dialog, _ -> dialog.dismiss() }
                        .create()
                        .show()
                }
            }

            binding.textviewFirst.text = "${binding.textviewFirst.text}\n${state.message}"
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}