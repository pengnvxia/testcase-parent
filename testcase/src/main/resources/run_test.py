import os
import datetime
from httprunner import HttpRunner
import sys
import json


def run_test(projectName,testcaseName):
# projectName = 'paper'
# testcaseName= 'testGroupName'
	testset_path = './yaml/' + projectName + '/' + testcaseName + '.yml'
	report_name = testcaseName
	kwargs = {
		"failfast": False,
	}
	runner = HttpRunner(**kwargs)
	runner.run(testset_path)
	for detail in runner.summary['details']:
		try:
			time_stamp = int(detail['time']['start_at'])
			detail['time']['start_at'] = datetime.datetime.fromtimestamp(time_stamp).strftime('%Y-%m-%d %H:%M:%S')
		except Exception:
			pass

		for record in detail['records']:
			try:
				time_stamp = int(record['meta_data']['request']['start_timestamp'])
				record['meta_data']['request']['start_timestamp'] = \
					datetime.datetime.fromtimestamp(time_stamp).strftime('%Y-%m-%d %H:%M:%S')
			except Exception:
				pass

	time_stamp = int(runner.summary["time"]["start_at"])
	runner.summary['time']['start_datetime'] = datetime.datetime.fromtimestamp(time_stamp).strftime('%Y-%m-%d %H:%M:%S')
	report_name = report_name if report_name else runner.summary['time']['start_datetime']
	runner.summary['html_report_name'] = report_name

	report_path = os.path.join(os.getcwd(), "reports{}{}{}.html".format('/', report_name + '/', report_name + '-' + str(
		int(runner.summary['time']['start_at']))))
	#生成报告
	runner.gen_html_report(report_name, html_report_template=os.path.join(os.getcwd(),
																		  "templates{}extent_report_template.html".format(
																			  '/')))

	with open(report_path, encoding='utf-8') as stream:
		reports = stream.read()

	test_reports = {
		"report_name": report_name,
		"status": runner.summary.get('success'),
		"successes": runner.summary.get('stat').get('successes'),
		"testsRun": runner.summary.get('stat').get('testsRun'),
		"start_at": runner.summary['time']['start_datetime'],
		"reports": reports
	}
	return json.dumps(test_reports)


if __name__ == '__main__':
	a = []
	for i in range(1, len(sys.argv)):
		a.append(sys.argv[i])
	print(run_test(a[0], a[1]))

