import os
import io
import yaml
import sys
import ast



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



