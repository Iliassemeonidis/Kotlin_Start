package com.example.kotlinstart.view.experiments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.*
import android.content.Context.BIND_AUTO_CREATE
import android.content.Context.NOTIFICATION_SERVICE
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kotlinstart.databinding.FragmentThreadBinding
import com.example.kotlinstart.view.experiments.BoundService.ServiceBinder
import kotlinx.android.synthetic.main.fragment_thread.*
import java.util.*


const val BROADCAST_ACTION_CALCFINISHED = "ru.geekbrains.service.calculationfinished"
private var isBound: Boolean = false
private var boundService: ServiceBinder? = null
class ThreadFragment : Fragment() {

    private var _binding: FragmentThreadBinding? = null
    private val binding get() = _binding!!
    private var counterThread = 0
    private val receiver = MainBroadcastReceiver()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThreadBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onStart() {
        super.onStart()
        requireActivity().registerReceiver(
            calculationFinishedReceiver,
            IntentFilter(BROADCAST_ACTION_CALCFINISHED)
        )
    }

    override fun onStop() {
        super.onStop()
        requireActivity().unregisterReceiver(receiver)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView();
        initNotificationChannel();
    }


    private fun initView() {
        binding.buttonStartService.setOnClickListener {
            requireContext().startService(Intent(requireContext(),MainService::class.java))

        }
        binding.buttonCalcService.setOnClickListener {
            val seconds = Integer.parseInt(binding.editSeconds.text.toString())
            // todo WTF is CalculationService ???
            //CalculationService.startCalculationService(requireContext(),seconds)
        }
        binding.buttonBindService.setOnClickListener {
            val intent = Intent(requireContext(), BoundService::class.java)
            requireContext().bindService(intent,boundServiceConnection, BIND_AUTO_CREATE)

        }
        binding.buttonNextFibo.setOnClickListener {
                if (boundService == null) {
                    textFibonacci.text = "Unbound service";
                } else {
                    val fibo = boundService?.getNextFibonacci()
                    textFibonacci.text = fibo.toString()
                }

        }
    }

    // На Android OS версии 2.6 и выше необходимо создать канал нотификации.
    // На старых версиях канал создавать не надо
    private fun initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager: NotificationManager =
                requireContext().getSystemService(NOTIFICATION_SERVICE) as NotificationManager;
            val importance = NotificationManager.IMPORTANCE_LOW
            val mChannel = NotificationChannel("2", "name", importance)
            notificationManager.createNotificationChannel(mChannel)
        }
    }


    // Получатель широковещательного сообщения
    private val calculationFinishedReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // todo WTF is CalculationService ???
            //val result = intent?.getLongExtra(CalculationService.EXTRA_RESULT, 0)

            // Потокобезопасный вывод данных
            binding.textView.post{
                run {
                    //binding.textView.text = result.toString()
                }
            }
        }
    }

    // Обработка соединения с сервисом
    private val boundServiceConnection: ServiceConnection = object : ServiceConnection {
        // При соединении с сервисом
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            boundService = service as ServiceBinder?
            isBound = boundService != null
        }

        // При разрыве соединения с сервисом
        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false;
            boundService = null;
        }
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = ThreadFragment().apply {}
    }
}


