//创建一个定时任务，每隔一分钟获取当前时间，并使用 Flight.AutoUpdateStatus() 方法更新航班状态

package frame;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import flight.*;


public class TimeThread implements Runnable {
	//// 声明一个线程控制标志，用于停止线程，使用 volatile 确保线程间可见性
	//volatile 关键字确保该变量在线程之间是可见的（不会因为线程缓存而造成数据不一致）
	public volatile boolean ThreadFlag = true;

	@Override
	public void run() {
		// 假设 df 是指定的日期时间格式
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
		while (ThreadFlag) {
			LocalDateTime now = LocalDateTime.now();
			// 使用 DateTimeFormatter 格式化成字符串
			//String formattedNow = now.format(df);
			Flight.AutoUpdateStatus(now);
			// 1s*60*5=5min
			int time = 1000 * 60 * 1;
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// main 方法示例，演示如何启动和停止 TimeThread 线程
	public static void main(String[] args) {
		// 创建 TimeThread 实例
		TimeThread updateBegin = new TimeThread();

		// 启动线程
		Thread thread = new Thread(updateBegin);
		thread.start();  // 启动线程

		// 定义一个计数器变量，用于模拟主程序逻辑
		long ing = 0;  // 计数器
		while(true) {  // 无限循环
			ing++;  // 增加计数器

			// 输出当前循环次数
			System.out.println("主程序循环次数：" + ing);

			try {
				// 线程休眠100毫秒，模拟主程序的执行
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// 捕获并处理中断异常
				e.printStackTrace();
			}

			// 每经过1000个循环（即 100 秒）
			if((ing % 1000) == 0) {
				// 停止 TimeThread 线程的标志
				updateBegin.ThreadFlag = false;  // 停止线程的标志
				System.out.println("线程已经停止");  // 输出线程停止信息

				// 等待线程完全停止
				try {
					thread.join();  // 等待线程结束
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// 退出循环
				break;
			}
		}
	}

}
