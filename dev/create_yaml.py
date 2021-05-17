import os
import io
import yaml
import sys
import ast


# testcase_list = [{"config":{"request":{"base_url":"http://172.19.21.228:58080"},"variables":{},"name":"test002","id":"test002","parameters":{}}},{"test":{"request":{"headers":{},"json":{},"params":{}},"variables":{},"name":"test002","validate":[{"eq":["content.name001",3]}]}}]
# project = 'paper'
# path = 'test002'


def create_yaml(project, path, testcase_list):
	root_path = './yaml'
	if not os.path.exists(root_path):
		os.makedirs(root_path)
	project_testcase_path = os.path.join(root_path, project)
	if not os.path.exists(project_testcase_path):
		os.makedirs(project_testcase_path)
	yaml_file = os.path.join(project_testcase_path, path+'.yml')
	with io.open(yaml_file, 'w', encoding='utf-8') as stream:
		yaml.dump(testcase_list, stream, indent=4, default_flow_style=False, encoding='utf-8')
	return 'ok'


if __name__ == '__main__':
	a = []
	for i in range(1, len(sys.argv)):
		if i == 3:
			a.append(ast.literal_eval(sys.argv[i]))
		else:
			a.append(sys.argv[i])

	print(create_yaml(a[0], a[1], a[2]))
