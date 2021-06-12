#!/usr/bin/python
# -*- coding: UTF-8 -*-
from base64 import b64encode
from collections import Iterable

from httprunner.compat import basestring, bytes, numeric_types
from jinja2 import escape

from httprunner import HttpRunner
import json
import sys

def stringify_data(meta_data, request_or_response):
    headers = meta_data[request_or_response]["headers"]

    request_or_response_dict = meta_data[request_or_response]

    for key, value in request_or_response_dict.items():

        if isinstance(value, list):
            value = json.dumps(value, indent=2, ensure_ascii=False)

        elif isinstance(value, bytes):
            try:
                encoding = meta_data["response"].get("encoding")
                if not encoding or encoding == "None":
                    encoding = "utf-8"
                # 处理cookie暂时先删除
                if meta_data["response"].__contains__("cookies"):
                    del meta_data["response"]["cookies"]

                content_type = meta_data["response"]["content_type"]
                if "image" in content_type:
                    meta_data["response"]["content_type"] = "image"
                    value = "data:{};base64,{}".format(
                        content_type,
                        b64encode(value).decode(encoding)
                    )
                else:
                    value = escape(value.decode(encoding))
            except UnicodeDecodeError:
                pass

        elif not isinstance(value, (basestring, numeric_types, Iterable)):
            # class instance, e.g. MultipartEncoder()
            value = repr(value)

        meta_data[request_or_response][key] = value


def run_test(projectName,testcaseName):
	# projectName = 'paper'
	# testcaseName= 'testExtract-可行'
    # testset_path = './yaml/' + projectName + '/' + testcaseName + '.yml'
    testset_path = '/opt/yaml/' + projectName + '/' + testcaseName + '.yml'
    report_name = testcaseName
    kwargs = {
	    "failfast": False,
    }
    runner = HttpRunner(**kwargs)
    runner.run(testset_path)
    for index, suite_summary in enumerate(runner.summary["details"]):
	    for record in suite_summary.get("records"):
		    meta_data = record['meta_data']
		    stringify_data(meta_data, 'request')
		    stringify_data(meta_data, 'response')

    return json.dumps(runner.summary['details'])


if __name__ == '__main__':
	a = []
	for i in range(1, len(sys.argv)):
		a.append(sys.argv[i])
	print(run_test(a[0], a[1]))

